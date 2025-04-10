package com.mstech.lifeline.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.ToastUtils
import com.mstech.lifeline.EasyLocation
import com.mstech.lifeline.R
import com.mstech.lifeline.api.RetrofitApi
import com.mstech.lifeline.databinding.ActivitySignupBinding
import com.mstech.lifeline.utils.Utils
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.FileDescriptor
import java.io.IOException
import java.io.InputStream
import java.util.regex.Pattern


class SignupActivity : AppCompatActivity() {
    var coordinaterList = ArrayList<String>()
    var coordinaterIdList = ArrayList<String>()
    var encodedImage: String = ""
    var image: Bitmap? = null
    var memberType = 1
    var coordinatorId = 0
    var lat: Double = 0.0
    var lon: Double = 0.0
    val GALLERY_REQUEST = 100

    private lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "SignUp"
        getGps()
//        lifecycleScope.launch {
//            getCoordinaters()
//        }
        binding.imgProfile.setOnClickListener {
            val cameraIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

//            val cameraIntent = Intent(Intent.ACTION_PICK,MediaStore.ACTION_IMAGE_CAPTURE)
            resultLauncher.launch(cameraIntent)

// Get your image

        }

        binding.coordinatorGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId -> // checkedId is the RadioButton selected
            val rb = findViewById<View>(checkedId) as RadioButton
//            Toast.makeText(applicationContext, rb.text, Toast.LENGTH_SHORT).show()
            if (rb.text.equals("Member")) {
                memberType = 1
                binding.cordinaterlayout.visibility = View.VISIBLE
                binding.cityLayout.visibility = View.GONE

            } else {
                memberType = 2
                binding.cordinaterlayout.visibility = View.GONE
                binding.cityLayout.visibility = View.VISIBLE
            }
        })

        binding.tvLogin.setOnClickListener {
            onBackPressed()
        }

        binding.submit.setOnClickListener(View.OnClickListener {
            if (!NetworkUtils.isConnected()) {
                ToastUtils.showShort("No Internet Connection")
            } else if (binding.firstname.text.isNullOrBlank()) {
                ToastUtils.showShort("Enter FirstName")
            } else if (binding.lastname.text.isNullOrBlank()) {
                ToastUtils.showShort("Enter LastName")
            }
//            else if (memberType != 2 && coordinaterIdList[binding.statespinner.selectedItemPosition] == "0") {
//                ToastUtils.showShort("Select Coordinator")
//            }
            else if (binding.emailid.text?.isNotEmpty() == true && !isValidEmail(binding.emailid.text.toString())) {
                ToastUtils.showShort("Enter Valid Email-Id")
            } else if (binding.mobile.text.isNullOrBlank()) {
                ToastUtils.showShort("Enter Mobile Number")
            } else if (binding.pin.text?.length != 4) {
                ToastUtils.showShort("Enter Pin")
            } else if (binding.address.text.isNullOrBlank()) {
                ToastUtils.showShort("Enter Address")
            } else if (binding.postalcode.text.isNullOrBlank()) {
                ToastUtils.showShort("Enter Postalcode")
            }else if (binding.referalCode.text.isNullOrBlank() || binding.referalCode.text.toString().length <6) {
                ToastUtils.showShort("Enter Referral Code")
            }else if (encodedImage.isEmpty()) {
                ToastUtils.showShort("Choose Profile Picture")
            } else {
//                if (memberType == 2) {
//                    coordinatorId = 0
//                } else {
//                    coordinatorId =
//                        coordinaterIdList[binding.statespinner.selectedItemPosition].toInt()
//                }
                lifecycleScope.launch {
                    KeyboardUtils.hideSoftInput(this@SignupActivity)
                    registerMember()
                }
            }
        })

    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result?.data != null) {
                    val image_uri: Uri = result.data!!.data!!
                    val imageStream: InputStream? = contentResolver.openInputStream(image_uri)
                    val selectedImage = BitmapFactory.decodeStream(imageStream)
                    encodedImage= encodeImage(selectedImage).toString()
                    binding.imgProfile.setImageURI(image_uri)
                }
            }
        }
    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b: ByteArray = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }


    private suspend fun getCoordinaters() {
        coordinaterList.clear()
        coordinaterIdList.clear()
        Utils.showProgress(this, true)
        val response =
            RetrofitApi().getCoordinaters("1")
        if (response.isSuccessful) {
            var data = response.body()
               coordinaterList.add(0,"Select Coordinator")
            coordinaterIdList.add("0")
            for (x in 0 until data?.size!!) {
                coordinaterList.add(data[x].Text.toString())
                coordinaterIdList.add(data[x].Id.toString())
            }
            val adapter =
                ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, coordinaterList)
            binding.statespinner.adapter = adapter
        } else {
            ToastUtils.showShort(response.errorBody().toString())
        }
        Utils.showProgress(this, false)
    }

    private fun isValidEmail(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
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

    private suspend fun registerMember() {
        Utils.showProgress(this, true)
        var obj = JSONObject()
        obj.put("MemberId", 0)
        obj.put("FirstName", binding.firstname.text.toString())
        obj.put("LastName", binding.lastname.text.toString())
        obj.put("EmailId", binding.emailid.text.toString())
        obj.put("Mobile", binding.mobile.text.toString())
        obj.put("UserId", binding.userId.text.toString())
        obj.put("Pin", binding.pin.text.toString())
        obj.put("PostCode", binding.postalcode.text.toString())
        obj.put("GeoAddress", binding.address.text.toString())
        obj.put("Latitude", lat)
        obj.put("Longitude", lon)
        obj.put("ProfilePic", encodedImage)
        obj.put("CountryId", 1)
        obj.put("StateId", 2)
        obj.put("Gender", 1)
        obj.put("LocationId", 0)
        obj.put("Status", 1)
        obj.put("ReferralCode", binding.referalCode.text.toString())

        var finalbody = ((obj)).toString()
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val response = RetrofitApi().register(finalbody)
        if (response.isSuccessful) {
            if (response.body()?.ResultId!! > 0) {
                ToastUtils.showShort(response.body()?.ResultMessage.toString())
                onBackPressed()
            } else {
                ToastUtils.showShort(response.body()?.ResultMessage.toString())
            }
        } else {
            ToastUtils.showShort(response.errorBody()?.string())
        }
        Utils.showProgress(this, false)
    }
}