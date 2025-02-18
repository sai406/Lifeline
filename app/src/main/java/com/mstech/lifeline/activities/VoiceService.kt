package com.mstech.lifeline.activities

import android.app.Notification
import android.app.*
import android.content.Intent
import android.os.*
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.birjuvachhani.locus.Locus
import com.blankj.utilcode.util.SPStaticUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.android.gms.maps.model.LatLng
import com.mstech.lifeline.R
import com.mstech.lifeline.api.RetrofitApi
import com.mstech.lifeline.models.SharedKey
import com.mstech.lifeline.utils.Utils
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class VoiceService : LifecycleService(), RecognitionListener {

    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var speechIntent: Intent
    private val CHANNEL_ID = "VoiceRecognitionServiceChannel"
    private var geoPoint: LatLng = LatLng(0.0, 0.0)

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(1, buildNotification())

        initSpeechRecognizer()
        Handler(Looper.getMainLooper()).postDelayed({
            Locus.configure {
                enableBackgroundUpdates = false
            }
            Locus.startLocationUpdates(this@VoiceService).observe(this@VoiceService) { result ->
                result.location?.let {
                    geoPoint = LatLng(
                        result?.location?.latitude ?: 0.0,
                        result?.location?.longitude ?: 0.0
                    )
                }
            }
        }, 5000)
    }

    private fun initSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizer.setRecognitionListener(this)

        speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)
        }

        startListening()
    }

    private fun startListening() {
        speechRecognizer.startListening(speechIntent)
    }

    override fun onResults(results: Bundle?) {
        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        matches?.let {
            for (result in it) {
                Log.d("VoiceService", "Recognized: $result")
                if (result.equals("help", ignoreCase = true)) {
//                    triggerApiCall()
                    lifecycleScope.launch{
                        sentSos()
                    }
                }
            }
        }
        startListening() // Restart listening to keep it continuous
    }

    override fun onError(error: Int) {
        Log.e("VoiceService", "Error: $error")
        startListening() // Restart listening after an error
    }

//    private fun triggerApiCall() {
//        val client = OkHttpClient()
//        val request = Request.Builder()
//            .url("https://your-api-url.com/help")
//            .post(RequestBody.create(MediaType.parse("application/json"), "{}"))
//            .build()
//
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                Log.e("API_CALL", "Failed: ${e.message}")
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                Log.d("API_CALL", "Success: ${response.body()?.string()}")
//            }private
//        })
//    }

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
            ToastUtils.showShort(response.body()?.string())
//            var obj = JSONObject(response.body()?.string())
//            var helpId = obj.getInt("HelpId")
//            startActivity(Intent(this, CameraActivity::class.java).putExtra("helpId",helpId).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK ))
        } else {
            ToastUtils.showShort(response.errorBody()?.string())
        }
        Utils.showProgress(this, false)

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Voice Recognition Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun buildNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Listening for 'Help'")
            .setContentText("Voice recognition is active.")
            .setSmallIcon(R.drawable.ic_baseline_campaign_24)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer.destroy()
    }


    // ✅ REQUIRED: Implement all RecognitionListener methods

    override fun onReadyForSpeech(params: Bundle?) {
        Log.d("VoiceService", "Ready for speech")
    }

    override fun onBeginningOfSpeech() {
        Log.d("VoiceService", "Speech started")
    }

    override fun onRmsChanged(rmsdB: Float) {
        // This method gets called continuously - no need to log it
    }

    override fun onBufferReceived(buffer: ByteArray?) {
        Log.d("VoiceService", "Buffer received")
    }

    override fun onEndOfSpeech() {
        Log.d("VoiceService", "End of speech")
        startListening() // Restart listening after speech ends
    }

    override fun onPartialResults(partialResults: Bundle?) {
        val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        matches?.let {
            for (result in it) {
                Log.d("VoiceService", "Partial result: $result")
                if (result.equals("help", ignoreCase = true)) {
//                    triggerApiCall()
                    lifecycleScope.launch{
                        sentSos()
                    }

                }
            }
        }
    }

    override fun onEvent(eventType: Int, params: Bundle?) {
        Log.d("VoiceService", "Event occurred: $eventType")
    }
}

