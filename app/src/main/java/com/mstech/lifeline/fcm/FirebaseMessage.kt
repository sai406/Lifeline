package com.mstech.lifeline.fcm

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mstech.lifeline.R
import com.mstech.lifeline.activities.SplashActivity
import com.mstech.lifeline.coordinater.activities.IncidentDetailsActivity

const val CHANNEL_ID = "NOTIFICATION_CHANNEL"
const val  CHANNEL_NAME = "com.example.fcmpushnotification"

class FirebaseMessage : FirebaseMessagingService(){
    private fun generateNotification(title: String, message: String, link: String){
        var intent: Intent? = null
        if (link.isNotEmpty()) {
            intent = Intent(this, IncidentDetailsActivity::class.java)
        } else {
            intent = Intent(this, SplashActivity::class.java)
        }

        // Assign channel ID
        val channel_id = "notification_channel"
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        var pendingIntent : PendingIntent
        //var pendingIntent =   PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent  = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        } else {
            pendingIntent =   PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        }

        //channel id, channel name
        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.alarm))
            .setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_VIBRATE)
        //Attach la notificacion creada a un layout custom
        builder = builder.setContent(getRemoteView(title, message))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //verificar si android es mayor a android Oreo
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0, builder.build())
    }


    @SuppressLint("RemoteViewLayout")
    private fun getRemoteView(title: String, message: String) : RemoteViews {
        val remoteView = RemoteViews(packageName, R.layout.notification)
        remoteView.setTextViewText(R.id.title, title)
        remoteView.setTextViewText(R.id.message, message)
        remoteView.setImageViewResource(R.id.image, R.drawable.ic_notifications_black_24dp)

        return remoteView
    }

    //mostrar la notificacion
    override fun onMessageReceived(message: RemoteMessage) {
        if(message.notification !=null){

            var body = ""
            var link = ""
            if (message.notification!!.body!!.contains("_")) {
                body = message.notification!!.body!!.split("_")[0]
                link = message.notification!!.body!!.split("_")[1]
            } else {
                body = message.notification!!.body!!
            }
            generateNotification(
                message.notification?.title?:"",
                body, link?:""
            )
//            generateNotification(
//                message.notification?.title?:"",
//                message.notification?.body?:"",
//                link?:""
//            )
        }
    }

}