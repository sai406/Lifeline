package com.mstech.lifeline.activities

import android.Manifest

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.SPStaticUtils
import com.blankj.utilcode.util.ToastUtils
import com.github.dhaval2404.imagepicker.ImagePicker
import com.mstech.lifeline.models.SharedKey
import com.mstech.lifeline.R
import com.mstech.lifeline.api.RetrofitApi
import com.mstech.lifeline.databinding.ActivityUserDetailsCaptureBinding
import com.mstech.lifeline.utils.Utils.showProgress
//import com.theartofdev.edmodo.cropper.CropImage
//import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.*
import java.util.*
import java.util.regex.Pattern


 public class UserDetailsCaptureActivity : AppCompatActivity() {
    lateinit var binding: ActivityUserDetailsCaptureBinding
    var locationList = ArrayList<String>()
    private var mImageByte: ByteArray? = null
    var locationIdList = ArrayList<String>()
    private var mCropImageUri: Uri? = null
    var pic_encoded =""
    lateinit var image : ImageView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailsCaptureBinding.inflate(layoutInflater)
        setContentView(binding.root)
        image = findViewById(R.id.profileimage)
        lifecycleScope.launch {
            getLocations()
        }

        binding.profileimage.setOnClickListener(View.OnClickListener {
            ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )    //Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        })

        binding.submit.setOnClickListener(View.OnClickListener {
            if (!NetworkUtils.isConnected()) {
                ToastUtils.showShort("No Internet Connection")
            } else if (binding.firstname.text.isNullOrBlank()) {
                ToastUtils.showShort("Enter FirstName")
            } else if (binding.lastname.text.isNullOrBlank()) {
                ToastUtils.showShort("Enter LastName")
            } else if (!isValidEmail(binding.emailid.text.toString())) {
                ToastUtils.showShort("Enter Email-Id")
            } else if (binding.statespinner.selectedItemPosition == 0) {
                ToastUtils.showShort("Select Location")
            } else if (binding.address.text.isNullOrBlank()) {
                ToastUtils.showShort("Enter Address")
            } else if (binding.postalcode.text.isNullOrBlank()) {
                ToastUtils.showShort("Enter Postalcode")
            } else {
                lifecycleScope.launch {
                    registerMember()
                }
            }
        })
    }

    fun onSelectImageClick(view: View?) {
//        CropImage.startPickImageActivity(this)
    }

   /* @SuppressLint("NewApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        // handle result of pick image chooser
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            val imageUri = CropImage.getPickImageResultUri(applicationContext, data)

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(applicationContext, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri)
            }
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {

                image.setImageURI(result.uri)
                val imageStream: InputStream?
                try {
                    imageStream = contentResolver.openInputStream(result.uri)
                    val selectedImage = BitmapFactory.decodeStream(imageStream)
                    pic_encoded = encodeImage(selectedImage)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(
                    applicationContext,
                    "Cropping failed: " + result.error,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
*/
    /**
     * Start crop image activity for the given image.
     */
   /* private fun startCropImageActivity(imageUri: Uri) {
        CropImage.activity(imageUri)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setAspectRatio(1, 1)
            .setFixAspectRatio(true)
            .start(this)
    }
*/
    private fun encodeImage(bm: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 20, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    private fun setActionBarTitle() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "New Chat"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> super.onBackPressed()
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private suspend fun registerMember() {
        var obj = JSONObject()
        obj.put("MemberId", "0")
        obj.put("FirstName", binding.firstname.text.toString())
        obj.put("LastName", binding.lastname.text.toString())
        obj.put("UserId", "")
        obj.put("Pin", "")
        obj.put("EmailId", binding.emailid.text.toString())
        obj.put("Mobile", SPStaticUtils.getString(SharedKey.MOBILE, ""))
        obj.put("PostCode", binding.postalcode.text.toString())
        obj.put("Latitude", "0.0")
        obj.put("Longitude", "0.0")
        obj.put("LocationId", locationIdList.get(binding.statespinner.selectedItemPosition))
        obj.put("GeoAddress", binding.address.text.toString())
        obj.put("ProfilePic", "")
        var finalbody = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            ((obj)).toString()
        )

        val response = RetrofitApi().registerMember(finalbody)
        if (response.isSuccessful) {
            if (response.body()?.statusCode!!>0){
                SPStaticUtils.put(SharedKey.CUSTOMER_ID, response.body()?.statusCode.toString())
                SPStaticUtils.put(SharedKey.ISLOGIN, true)
                ToastUtils.showShort("Signup Successfull")
                startActivity(Intent(this, AddSosActivity::class.java))
            }else{
                ToastUtils.showShort(response.body()?.statusMessage.toString())
            }


        } else {
            ToastUtils.showShort(response.errorBody()?.string())
        }
        showProgress(this, false)
    }

    private fun isValidEmail(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }
    private suspend fun getLocations() {
        showProgress(this, true)
        val response = RetrofitApi().getLocations()
        locationList.add("Select Nearby Location")
        locationIdList.add("0")
        if (response.isSuccessful) {
            var data = response.body()
            for (x in 0 until data?.size!!){
                locationList.add(data.get(x).Location.toString())
                locationIdList.add(data.get(x).LocationId.toString())
            }
            val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, locationList)
            binding.statespinner.adapter = adapter
        } else {
            ToastUtils.showShort(response.errorBody().toString())
        }
        showProgress(this, false)
    }

}