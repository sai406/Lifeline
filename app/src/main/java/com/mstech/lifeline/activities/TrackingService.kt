package com.mstech.lifeline.activities

import android.Manifest
import android.app.*
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.blankj.utilcode.util.SPStaticUtils
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.mstech.lifeline.models.SharedKey
import com.mstech.lifeline.R


class TrackingService : Service(), OnCompleteListener<Void?> {
    var canGetLocation = false
    var isGPSEnabled = false
    var alertDialog: AlertDialog.Builder? = null
    var handler: Handler? = null
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        buildNotification()
        requestLocationUpdates()
        //        Toast.makeText(TrackingService.this, "Tracking Started", Toast.LENGTH_SHORT).show();
    }

    private fun buildNotification() {
        val stop = "stop"
        //        registerReceiver(stopReceiver, new IntentFilter(stop));
        val broadcastIntent = PendingIntent.getBroadcast(
            this, 0, Intent("start"), PendingIntent.FLAG_UPDATE_CURRENT
        )
        // Create the persistent notification
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channelId = getString(R.string.app_name)
        var notificationChannel: NotificationChannel? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel =
                NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel.description = channelId
            notificationChannel.setSound(null, null)
            notificationManager.createNotificationChannel(notificationChannel)
            val notification: Notification = Notification.Builder(this, channelId)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Tracking is Going On")
                .setSmallIcon(R.drawable.logo)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .build()
            startForeground(1, notification)
        }
    }

    fun requestLocationUpdates() {
        locationManager = this@TrackingService
            .getSystemService(LOCATION_SERVICE) as LocationManager
        isGPSEnabled = locationManager!!
            .isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (isGPSEnabled) {
            val request = LocationRequest()
            //            String jobids = SharedPreferenceUtils.getStringFromSP(TrackingService.this, AppConstants.Runningjobs, "");

            request.interval = 100000

            request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            val client = LocationServices.getFusedLocationProviderClient(this)
            val permission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            if (permission == PackageManager.PERMISSION_GRANTED) {
                client.requestLocationUpdates(request, object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        val location = locationResult.lastLocation
                        location?.let {
                            trackingHistory(it)
                        }
                    }
                }, null)
            }
        } else {
            Toast.makeText(this@TrackingService, "Please Enable GPS", Toast.LENGTH_SHORT).show()
        }
    }

     fun trackingHistory(location: Location?) {
        var queue = Volley.newRequestQueue(this)
         val request = StringRequest(Request.Method.GET, "http://civiccare.net/api/StartCampaign?mid="+SPStaticUtils.getString(
             SharedKey.CUSTOMER_ID)+"&campid="+SPStaticUtils.getString(SharedKey.CAMPID)+"&latitude="+location?.latitude+"&longitude="+location?.longitude , object : Response.Listener<String?> {

             override fun onResponse(response: String?) {
                 Log.d("success", "success")
             }
         }, object : Response.ErrorListener {
             override fun onErrorResponse(error: VolleyError) {
                 Log.d("error", error.toString())
             }
         })
         queue.add(request)
    }


    override fun onComplete(task: Task<Void?>) {}

    companion object {
        private val TAG = TrackingService::class.java.simpleName
        var locationManager: LocationManager? = null
    }
}
