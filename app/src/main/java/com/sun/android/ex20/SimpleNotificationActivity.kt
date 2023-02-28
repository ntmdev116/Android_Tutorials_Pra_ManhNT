package com.sun.android.ex20

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sun.android.R
import com.sun.android.databinding.ActivitySimpleNotificationBinding


class SimpleNotificationActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySimpleNotificationBinding.inflate(layoutInflater) }
    private val notificationManager by lazy { getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
    private val mReceiver by lazy { NotificationReceiver() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setListeners()
        createNotificationChannel()
        registerReceiver(mReceiver, IntentFilter(ACTION_UPDATE_NOTIFICATION))
        setNotificationButtonState(isNotifyEnabled = true, isUpdateEnabled = false, isCancelEnabled = false)
    }

    override fun onDestroy() {
        unregisterReceiver(mReceiver)
        super.onDestroy()
    }

    private fun setListeners() {
        binding.notify.setOnClickListener(::sendNotification)
        binding.update.setOnClickListener(::modifyNotification)
        binding.cancel.setOnClickListener(::cancelNotification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(PRIMARY_CHANNEL_ID, name, importance).apply {
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                description = descriptionText
            }

            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(view: View) {
        val updateIntent = Intent(ACTION_UPDATE_NOTIFICATION)
        val updatePendingIntent = PendingIntent.getBroadcast(
            this,
            NOTIFICATION_ID, updateIntent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        val builder =
            getNotificationBuilder()
                .addAction(R.drawable.ic_status,
                    getString(R.string.update_me), updatePendingIntent);

        with(NotificationManagerCompat.from(this)) {
            notify(NOTIFICATION_ID, builder.build())
        }
        setNotificationButtonState(isNotifyEnabled = false, isUpdateEnabled = true, isCancelEnabled = true)
    }

    private fun modifyNotification(view: View) {
        updateNotification()
    }

    private fun updateNotification() {
        val androidImage = BitmapFactory
            .decodeResource(resources, R.drawable.mascot_1)

        val notifyBuilder =
            getNotificationBuilder()
                .setStyle(
                    NotificationCompat.BigPictureStyle()
                        .bigPicture(androidImage)
                        .setBigContentTitle(getString(R.string.notification_updated))
                )

        notificationManager.notify(NOTIFICATION_ID, notifyBuilder.build())

        setNotificationButtonState(isNotifyEnabled = false, isUpdateEnabled = false, isCancelEnabled = true)
    }

    private fun cancelNotification(view: View) {
        notificationManager.cancel(NOTIFICATION_ID)

        setNotificationButtonState(isNotifyEnabled = true, isUpdateEnabled = false, isCancelEnabled = false)
    }

    private fun getNotificationBuilder(): NotificationCompat.Builder {
        val notificationIntent = Intent(this, this::class.java)
        val notificationPendingIntent = PendingIntent.getActivity(
            this, NOTIFICATION_ID, notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_text))
            .setSmallIcon(R.drawable.ic_favorite)
            .setAutoCancel(true).setContentIntent(notificationPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
    }

    private fun setNotificationButtonState(isNotifyEnabled: Boolean, isUpdateEnabled: Boolean, isCancelEnabled: Boolean) {
        binding.notify.isEnabled = isNotifyEnabled
        binding.update.isEnabled = isUpdateEnabled
        binding.cancel.isEnabled = isCancelEnabled
    }

    inner class NotificationReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            updateNotification()
        }
    }

    companion object {
        private val NOTIFICATION_ID = 0
        private val PRIMARY_CHANNEL_ID = "primary_notification_channel"
        private val ACTION_UPDATE_NOTIFICATION = "com.sun.android.ACTION_UPDATE_NOTIFICATION"
    }
}
