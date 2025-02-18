package com.mstech.lifeline.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class UnlockReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_USER_PRESENT == intent.action) {
            // The device is unlocked, start your app here
            val launchIntent = context.packageManager.getLaunchIntentForPackage("com.mstech.lifeline")
            if (launchIntent != null) {
                context.startActivity(launchIntent)
            }
        }
    }
}