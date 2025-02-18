package com.mstech.lifeline.fcm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mstech.lifeline.R
import com.mstech.lifeline.activities.SplashActivity
import com.mstech.lifeline.coordinater.activities.IncidentDetailsActivity


class MyFirebaseMessagingService : FirebaseMessagingService() {
    // Override onMessageReceived() method to extract the
    // title and
    // body from the message passed in FCM

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        // Second case when notification payload is
        // received.
        if (remoteMessage.notification != null) {
            // Since the notification is received directly from
            // FCM, the title and the body can be fetched
            // directly as below.
            var body = ""
            var link = ""
            if (remoteMessage.notification!!.body!!.contains("~")) {
                body = remoteMessage.notification!!.body!!.split("~")[0]
                link = remoteMessage.notification!!.body!!.split("~")[1]
            } else {
                body = remoteMessage.notification!!.body!!
            }
            showNotification(
                remoteMessage.notification!!.title,
                body, link
            )
        }
    }

    // Method to get the custom Design for the display of
    // notification.

    // Method to display the notifications
    @RequiresApi(Build.VERSION_CODES.M)
    fun showNotification(
        title: String?,
        message: String?,
        link: String
    ) {
        // Pass the intent to switch to the MainActivity
        var intent: Intent? = null
        if (link.isNotEmpty()) {
            intent = Intent(this, IncidentDetailsActivity::class.java)
        } else {
            intent = Intent(this, SplashActivity::class.java)
        }

        // Assign channel ID
        val channel_id = "notification_channel"
        // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
        // the activities present in the activity stack,
        // on the top of the Activity that is to be launched
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.setData(Uri.parse(link))
        // Pass the intent to PendingIntent to start the
        // next Activity
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        // Create a Builder object using NotificationCompat
        // class. This will allow control over all the flags
        var builder: NotificationCompat.Builder = NotificationCompat.Builder(
            applicationContext,
            channel_id
        )
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setVibrate(
                longArrayOf(
                    1000, 1000, 1000,
                    1000, 1000
                )
            )
            .setOnlyAlertOnce(true)
            .setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.alarm))
            .setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_VIBRATE)
            .setContentIntent(pendingIntent)

        // A customized design for the notification can be
        // set only for Android versions 4.1 and above. Thus
        // condition for the same is checked here.

        builder = builder.setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.mipmap.ic_launcher)

        // Create an object of NotificationManager class to
        // notify the
        // user of events that happen in the background.
        val notificationManager = getSystemService(
            NOTIFICATION_SERVICE
        ) as NotificationManager
        // Check if the Android Version is greater than Oreo
        if (Build.VERSION.SDK_INT
            >= Build.VERSION_CODES.O
        ) {
            val notificationChannel = NotificationChannel(
                channel_id, "web_app",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.alarm),Notification.AUDIO_ATTRIBUTES_DEFAULT)

            notificationManager.createNotificationChannel(
                notificationChannel
            )
        }
        notificationManager.notify(0, builder.build())
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun handleIntent(intent: Intent) {
        try {
            if (intent.extras != null) {
                val builder = RemoteMessage.Builder("FirebaseMessageReceiver")
                for (key in intent.extras!!.keySet()) {
                    builder.addData(key!!, intent.extras!![key].toString())
                }
                onMessageReceived(builder.build())
            } else {
                super.handleIntent(intent)
            }
        } catch (e: Exception) {
            super.handleIntent(intent)
        }
    }


}