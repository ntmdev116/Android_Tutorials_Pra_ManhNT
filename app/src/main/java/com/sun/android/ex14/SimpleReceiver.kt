package com.sun.android.ex14

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.sun.android.BuildConfig
import com.sun.android.R

const val ACTION_CUSTOM_BROADCAST: String = BuildConfig.APPLICATION_ID + ".ACTION_CUSTOM_BROADCAST"

class SimpleReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val intentAction = intent.action

        var toastMessage = R.string.unknown_intent_action

        when (intentAction) {
            Intent.ACTION_POWER_DISCONNECTED -> { toastMessage = R.string.power_connected }
            Intent.ACTION_POWER_CONNECTED -> { toastMessage = R.string.power_disconnected }
            ACTION_CUSTOM_BROADCAST -> { toastMessage = R.string.custom_receiver }
        }

        Toast.makeText(context, toastMessage.toString(), Toast.LENGTH_SHORT).show()
    }
}
