package com.sun.android.ex14

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.sun.android.databinding.ActivitySimplePowerReceiverBinding


class SimplePowerReceiverActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySimplePowerReceiverBinding.inflate(layoutInflater) }

    private val mSimpleReceiver by lazy { SimpleReceiver() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setReceiver()

        binding.buttonSendBroadcast.setOnClickListener { sendBroadcast() }
    }

    override fun onDestroy() {
        unregisterReceiver(mSimpleReceiver)
        LocalBroadcastManager.getInstance(this)
            .unregisterReceiver(mSimpleReceiver);
        super.onDestroy()
    }

    private fun setReceiver() {
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED)
        filter.addAction(Intent.ACTION_POWER_CONNECTED)
        this.registerReceiver(mSimpleReceiver, filter)

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(mSimpleReceiver, IntentFilter(ACTION_CUSTOM_BROADCAST))
    }

    private fun sendBroadcast() {
        val customBroadcastIntent = Intent(ACTION_CUSTOM_BROADCAST)
        LocalBroadcastManager.getInstance(this).sendBroadcast(customBroadcastIntent)
    }
}
