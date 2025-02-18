package com.mstech.lifeline.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.SPStaticUtils
import com.blankj.utilcode.util.ToastUtils
import com.mstech.lifeline.models.SharedKey
import com.mstech.lifeline.EasyLocation
import com.mstech.lifeline.R
import com.mstech.lifeline.activities.Messagescreen
import com.mstech.lifeline.activities.SOSSentActivity
import com.mstech.lifeline.activities.SupportActivity
import com.mstech.lifeline.api.RetrofitApi
import com.mstech.lifeline.databinding.FragmentHomeBinding
import com.mstech.lifeline.utils.Utils
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

import java.io.ByteArrayOutputStream

import java.io.InputStream
import org.json.JSONArray


class HomeFragment : Fragment(R.layout.fragment_home) {
    lateinit var binding: FragmentHomeBinding
    var address: String = ""
    var lat: Double = 0.0
    var lon: Double = 0.0
    var postal: String = ""
    lateinit var photoURI: Uri
    var helpId=0

    private val ALL_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.POST_NOTIFICATIONS,
    )
    var ConnInfo: StringBuilder = StringBuilder()
    val CAMERA_REQUEST_CODE = 102
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.sos.setOnClickListener(View.OnClickListener {
//            binding.confirm.visibility = View.VISIBLE
//            binding.cancel.visibility = View.VISIBLE
//            binding.sos.visibility = View.INVISIBLE
//            binding.textView.setText("We will send your details to support team. If you clicked on by mistake press Cancel.")
            startActivity(Intent(requireContext(), SOSSentActivity::class.java))
//            lifecycleScope.launch {
//                delay(10000)
//                if (binding.confirm.visibility == View.VISIBLE) {
//                    sentSos()
//                }
//            }
        })
        binding.cancel.setOnClickListener(View.OnClickListener {
            binding.confirm.visibility = View.GONE
            binding.cancel.visibility = View.GONE
            binding.sos.visibility = View.VISIBLE
            binding.textView.setText(requireActivity().resources.getString(R.string.emergency_text))
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
        binding.social.setOnClickListener(View.OnClickListener {
            if (NetworkUtils.isConnected()) {
                startActivity(Intent(requireContext(), Messagescreen::class.java))
            } else {
                ToastUtils.showShort("No Internet Connection")
            }
        })
        binding.support.setOnClickListener(View.OnClickListener {
            if (NetworkUtils.isConnected()) {
                startActivity(Intent(requireContext(), SupportActivity::class.java))
            } else {
                ToastUtils.showShort("No Internet Connection")
            }
        })

//        var data = requireContext().getExternalFilesDirs(Environment.DIRECTORY_MOVIES)
//        for (element in data){
//            var path = element.parentFile
//            Log.d("TAG", "onCreateView: "+path.absolutePath)
//        }


        if (!PermissionUtils.isGranted(*ALL_PERMISSIONS)) {
            PermissionUtils
                .permission(*ALL_PERMISSIONS)
                .callback(object : PermissionUtils.SimpleCallback {
                    override fun onGranted() {
                        getGps()
                    }

                    override fun onDenied() {
                        ToastUtils.showShort("Accept all permissions to access app")
                    }
                }).request()
        } else {
            getGps()
        }


        /* binding.video.setOnClickListener(View.OnClickListener {
             if (!PermissionUtils.isGranted(*ALL_PERMISSIONS)) {
                 PermissionUtils
                     .permission(*ALL_PERMISSIONS)
                     .callback(object : PermissionUtils.SimpleCallback {
                         override fun onGranted() {
                             dispatchTakePictureIntent()
                         }

                         override fun onDenied() {
                             ToastUtils.showShort("Accept all permissions to access app")
                         }
                     }).request()
             } else {
                 dispatchTakePictureIntent()
             }
         })*/
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    ConvertToString(photoURI)

                } catch (e: Exception) {
                    e.printStackTrace()
                    ToastUtils.showShort(e.toString())
                }

            }
        }

    }
    fun ConvertToString(uri: Uri) {
        try {
            val input: InputStream? = requireActivity().getContentResolver().openInputStream(uri)
            var bytes = input?.let { getBytes(it) }
//            val ansValue: String = requireActivity().Base64.encodeToString(bytes, Base64.DEFAULT)
            val basedata: String = Base64.encodeToString(bytes,Base64.DEFAULT)
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
        val storageDir: File? = requireActivity().getExternalFilesDir(Environment.DIRECTORY_MOVIES)
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
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
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
                    requireContext(),
                    "com.mstech.lifeline.fileprovider",
                    photoFile
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//                takePictureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//                takePictureIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 120) //15s Limit
//                takePictureIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, (5*1048*1048).toString() +"L");
              this.startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)
            }else{
                ToastUtils.showShort("photo is null")
            }
        }

    }

    fun getGps() {
        EasyLocation(requireActivity(), object : EasyLocation.EasyLocationCallBack {
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
        Utils.showProgress(requireActivity(), true)
        var pincode=""
        if (lat!=0.0&&lon !=0.0) {
             pincode = getPostalCodeByCoordinates(requireActivity(),lat ,lon);
        }
        var obj = JSONObject()
        obj.put("MemberId", SPStaticUtils.getString(SharedKey.CUSTOMER_ID))
        obj.put("Message", "")
        obj.put("Image", "")
        obj.put("Latitude", lat.toString())
        obj.put("Longitude", lon.toString())
        obj.put("Postcode",pincode )

        var finalbody = ((obj)).toString()
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
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
            dispatchTakePictureIntent()
        }
        Utils.showProgress(requireContext(), false)

    }
    private suspend fun sendVideo(basedata: String) {
        Utils.showProgress(requireActivity(), true)

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
        Utils.showProgress(requireContext(), false)

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
}
