package com.mstech.lifeline.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.SPStaticUtils
import com.blankj.utilcode.util.ToastUtils
import com.mstech.lifeline.models.SharedKey
import com.mstech.lifeline.EasyLocation
import com.mstech.lifeline.R
import com.mstech.lifeline.api.RetrofitApi
import com.mstech.lifeline.databinding.ActivitySossentBinding
import com.mstech.lifeline.utils.Utils
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class SOSSentActivity : AppCompatActivity() {
    lateinit var binding: ActivitySossentBinding
    var address: String = ""
    var lat: Double = 0.0
    var lon: Double = 0.0
    var postal: String = ""
    lateinit var photoURI: Uri
    var helpId = 0

    private val ALL_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO,
    )
    var ConnInfo: StringBuilder = StringBuilder()
    val CAMERA_REQUEST_CODE = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySossentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.confirm.visibility = View.VISIBLE
        binding.cancel.visibility = View.VISIBLE
        binding.sos.visibility = View.INVISIBLE
        binding.textView.setText("We will send your details to support team. If you clicked on by mistake press Cancel.")
        binding.countText.visibility = View.VISIBLE
        supportActionBar?.hide()
        val timer = object: CountDownTimer(6000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.countText.text = "Auto Confirms in : " + millisUntilFinished / 1000+"s"
            }

            override fun onFinish() {
                binding.countText.text = "Sending SOS!"
                if (binding.confirm.visibility == View.VISIBLE) {
                    lifecycleScope.launch{
                        sentSos()
                    }

                }
            }

        }
        timer.start()
        binding.includeTop.ivBack.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        binding.includeTop.tvHeader.setText("SOS")
/*        lifecycleScope.launch {
            delay(10000)
            if (binding.confirm.visibility == View.VISIBLE) {
                sentSos()
            }
        }*/
        binding.sos.setOnClickListener(View.OnClickListener {
            binding.countText.visibility = View.VISIBLE
            binding.confirm.visibility = View.VISIBLE
            binding.cancel.visibility = View.VISIBLE
            binding.sos.visibility = View.INVISIBLE
            binding.textView.setText("We will send your details to support team. If you clicked on by mistake press Cancel.")
            timer.cancel()
            timer.start()
        })
        getGps()
        binding.cancel.setOnClickListener(View.OnClickListener {
            binding.confirm.visibility = View.GONE
            binding.cancel.visibility = View.GONE
            binding.sos.visibility = View.VISIBLE
            binding.countText.visibility = View.GONE
            binding.textView.setText(resources.getString(R.string.emergency_text))
            timer.cancel()
        })
        binding.confirm.setOnClickListener(View.OnClickListener {

            if (NetworkUtils.isConnected()) {
                lifecycleScope.launch {
                    sentSos()
                }
            } else {
                ToastUtils.showShort("No Internet Connection")
            }
        })

    }


    fun ConvertToString(uri: Uri) {
        try {
            val input: InputStream? = this.getContentResolver().openInputStream(uri)
            var bytes = input?.let { getBytes(it) }
//            val ansValue: String = requireActivity().Base64.encodeToString(bytes, Base64.DEFAULT)
            val basedata: String = Base64.encodeToString(bytes, Base64.DEFAULT)
            lifecycleScope.launch {
                sendVideo(basedata)
            }

        } catch (e: java.lang.Exception) {
            // TODO: handle exception
            e.printStackTrace()
            Log.d("error", "onActivityResult: $e")
        }
    }

    @Throws(IOException::class)
    fun getBytes(inputStream: InputStream): ByteArray? {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len = 0
        while (inputStream.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }

    @Throws(IOException::class)
    fun createImageFile(): File {
        // Create an image file name
        val storageDir: File? = this.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
        return File.createTempFile(
            "Lifeline_${getDateTime()}_", /* prefix */
            ".mp4", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
        }
    }

    private fun getDateTime(): String? {
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = Date()
        return dateFormat.format(date)
    }

    fun dispatchTakePictureIntent() {

        val takePictureIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(this.packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: Exception) {

                ToastUtils.showShort(ex.toString())
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(
                    this,
                    "com.mstech.lifeline.fileprovider",
                    photoFile
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                takePictureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                takePictureIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 120) //15s Limit
//                takePictureIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, (5*1048*1048).toString() +"L");
//                this.startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)
//                startForResult.launch(takePictureIntent)

                startActivity(Intent(this,CameraActivity::class.java).putExtra("helpId",helpId))
                finish()
            } else {
                ToastUtils.showShort("photo is null")
            }
        }

    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    ConvertToString(photoURI)

                } catch (e: Exception) {
                    e.printStackTrace()
                    ToastUtils.showShort(e.toString())
                }
                // Handle the Intent
                //do stuff here
            }
        }

    fun getGps() {
        EasyLocation(this, object : EasyLocation.EasyLocationCallBack {
            override fun permissionDenied() {
                Log.i("Location", "permission  denied")
                getGps()

            }

            override fun locationSettingFailed() {
                Log.i("Location", "setting failed")
            }

            override fun getLocation(location: Location) {
                Log.i(
                    "Location_lat_lng",
                    " latitude ${location.latitude} longitude ${location.longitude}"
                )

                lat = location.latitude
                lon = location.longitude


            }
        })
    }

    private suspend fun sentSos() {
        Utils.showProgress(this, true)
        var pincode = ""
        binding.countText.visibility = View.GONE
        if (lat != 0.0 && lon != 0.0) {
            pincode = getPostalCodeByCoordinates(this, lat, lon);
        }

        var obj = JSONObject()
        obj.put("MemberId", SPStaticUtils.getString(SharedKey.CUSTOMER_ID))
        obj.put("Message", "")
        obj.put("Image", "")
        obj.put("Latitude", lat.toString())
        obj.put("Longitude", lon.toString())
        obj.put("Postcode", pincode)

        var finalbody = ((obj)).toString()
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        if (SPStaticUtils.getString(SharedKey.ISVOLUNTEER).equals("2")) {
            val response = RetrofitApi().sentCoordinaterSos(finalbody)
            if (response.isSuccessful) {
                var obj = JSONObject(response.body()?.string())
                helpId = obj.getInt("HelpId")
                binding.confirm.visibility = View.GONE
                binding.cancel.visibility = View.GONE
                binding.sos.visibility = View.VISIBLE
                ToastUtils.showShort("Sent Successfull")
                lifecycleScope.launch {
                    rescueAction(helpId.toString())
                }

            } else {
                ToastUtils.showShort(response.errorBody()?.string())
//            dispatchTakePictureIntent()
            }
        } else {
            val response = RetrofitApi().sentSos(finalbody)
            if (response.isSuccessful) {
                var obj = JSONObject(response.body()?.string())
                helpId = obj.getInt("HelpId")
                binding.confirm.visibility = View.GONE
                binding.cancel.visibility = View.GONE
                binding.sos.visibility = View.VISIBLE
                ToastUtils.showShort("Sent Successfull")
                dispatchTakePictureIntent()
            } else {
                ToastUtils.showShort(response.errorBody()?.string())
//            dispatchTakePictureIntent()
            }
            Utils.showProgress(this, false)
        }
    }

    private suspend fun sendVideo(basedata: String) {
        Utils.showProgress(this, true)

        var obj = JSONObject()
        obj.put("MemberId", SPStaticUtils.getString(SharedKey.CUSTOMER_ID))
        obj.put("HelpId", helpId)
        val jsonArray = JSONArray()
        jsonArray.put(basedata)
        obj.put("Videos", jsonArray)

        var finalbody = ((obj)).toString()
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val response = RetrofitApi().addVideo(finalbody)
        if (response.isSuccessful) {
            ToastUtils.showShort("Video Sent Successfull")
        } else {
            ToastUtils.showShort(response.errorBody()?.string())
        }
        Utils.showProgress(this, false)

    }


    fun getPostalCodeByCoordinates(context: Context, lat: Double, lon: Double): String {
        val mGeocoder = Geocoder(context, Locale.getDefault())
        var zipcode: String? = null
        var address: Address? = null
        if (mGeocoder != null) {
            val addresses: List<Address>? = mGeocoder.getFromLocation(lat, lon, 5)
            if (addresses != null && addresses.size > 0) {
                for (i in addresses.indices) {
                    address = addresses[i]
                    if (address.getPostalCode() != null) {
                        zipcode = address.getPostalCode()
                        Log.d("TAG", "Postal code: " + address.getPostalCode())
                        break
                    }
                }
                return zipcode.toString()
            }
        }
        return ""
    }


    suspend fun rescueAction(helpId: String) {
        var response = RetrofitApi().rescueButton(helpId, 1)
        if (response.isSuccessful) {
            var data = (response.body())
            if (data?.statusCode!! > 0) {
                ToastUtils.showShort("Successfull")
            } else {
                ToastUtils.showShort("Try Again")
            }
            dispatchTakePictureIntent()
        } else {
            ToastUtils.showShort(response.errorBody()?.string())
        }
        Utils.showProgress(this, false)
    }


}