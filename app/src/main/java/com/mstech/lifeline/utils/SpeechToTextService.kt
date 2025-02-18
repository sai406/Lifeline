package com.mstech.lifeline.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.birjuvachhani.locus.Locus
import com.blankj.utilcode.util.SPStaticUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.android.gms.maps.model.LatLng
import com.mstech.lifeline.models.SharedKey
import com.mstech.lifeline.R
import com.mstech.lifeline.activities.CameraActivity
import com.mstech.lifeline.api.RetrofitApi
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import safety.com.br.android_shake_detector.core.ShakeDetector
import safety.com.br.android_shake_detector.core.ShakeOptions


class SpeechToTextService : LifecycleService() {

    private val NOTIFICATION_ID = 1
    private val CHANNEL_ID = "SpeechToTextServiceChannel"
    private var shakeDetector: ShakeDetector? = null
    private var geoPoint: LatLng = LatLng(0.0, 0.0)

    private var unlockReceiver : UnlockReceiver? = null
    override fun onCreate() {
        super.onCreate()
        startForeground(NOTIFICATION_ID, createNotification())
        val options = ShakeOptions()
            .background(false)
            .interval(5000)
            .shakeCount(2)
            .sensibility(5.0f)

        this.shakeDetector = ShakeDetector(options).start(
            this
        ) {
            Log.d("event", "onShake")
            lifecycleScope.launch{
                sentSos()
                unlockReceiver = UnlockReceiver()
                val filter = IntentFilter(Intent.ACTION_USER_PRESENT)
                registerReceiver(unlockReceiver, filter)
            }

        }

        Handler(Looper.getMainLooper()).postDelayed({
            Locus.configure {
                enableBackgroundUpdates = false
            }
            Locus.startLocationUpdates(this).observe(this@SpeechToTextService) { result ->
                result.location?.let {
                    geoPoint = LatLng(
                        result?.location?.latitude ?: 0.0,
                        result?.location?.longitude ?: 0.0
                    )
                }
            }
        }, 5000)

    }


    private suspend fun sentSos() {
        Utils.showProgress(this, true)
        var pincode = ""

        var obj = JSONObject()
        obj.put("MemberId", SPStaticUtils.getString(SharedKey.CUSTOMER_ID))
        obj.put("Message", "")
        obj.put("Image", "")
        obj.put("Latitude", geoPoint.latitude.toString())
        obj.put("Longitude", geoPoint.longitude.toString())
        obj.put("Postcode", "")

        var finalbody = ((obj)).toString()
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            val response = RetrofitApi().sentSos(finalbody)
            if (response.isSuccessful) {
                var obj = JSONObject(response.body()?.string())
                var helpId = obj.getInt("HelpId")
//                startActivity(Intent(this, CameraActivity::class.java).putExtra("helpId",helpId).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK ))
            } else {
                ToastUtils.showShort(response.errorBody()?.string())
            }
            Utils.showProgress(this, false)

    }

    private fun createNotification(): Notification {
        createNotificationChannel()

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("SOS HelpLine")
            .setContentText("Listening to Shake in the background")
            .setSmallIcon(R.mipmap.ic_launcher)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Speech To Text Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }
}
