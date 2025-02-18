package com.mstech.lifeline.vault

import android.Manifest
import android.content.ContentUris
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.FileUtils
import com.mstech.lifeline.databinding.ActivityVaultBinding
import com.mstech.lifeline.utils.RealPathUtill
import kotlinx.coroutines.launch


class VaultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVaultBinding
    // Initializing the layout views

    private lateinit var pdfUri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVaultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (!checkPermissions()) {
            requestPermissions()
        }
        // Setting click listener to the image TextView
        binding.imageView.setOnClickListener {
            selectImage()
        }

        // Setting click listener to the ImageView
        binding.imageTextView.setOnClickListener {
           imagePicker()
        }

        // Setting click listener to the PDF TextView
        binding.selectedPdf.setOnClickListener {
            selectPdf()
        }
    }
    fun checkPermissions(): Boolean {
        val hasReadPermissions = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        return hasReadPermissions
    }

    fun requestPermissions() {
        val permissionToRequest = mutableListOf<String>()
        permissionToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(this, permissionToRequest.toTypedArray(), 0)
    }
    private fun selectImage() {
        // Creating AlertDialog
        val choice = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val myAlertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        myAlertDialog.setTitle("Select Image")
        myAlertDialog.setItems(choice, DialogInterface.OnClickListener { dialog, item ->
            when {
                choice[item] == "Choose from Gallery" -> {
                    val pickFromGallery = Intent(
                        Intent.ACTION_GET_CONTENT,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                    pickFromGallery.type = "/image"
                    startActivityForResult(pickFromGallery, 1)
                }
                choice[item] == "Take Photo" -> {
                    val cameraPicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(cameraPicture, 0)
                }
                choice[item] == "Cancel" -> {
//                            myAlertDialog.()
                }
            }
        })
        myAlertDialog.show()
    }

    // Intent for openning files
    private fun selectPdf() {
        val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
        pdfIntent.type = "application/pdf"
        pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(pdfIntent, 12)
    }
    fun imagePicker() {
        val collection=  MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val pickIntent = Intent(Intent.ACTION_PICK,collection)
        pickIntent.type = "image/*"
        pickIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(Intent.createChooser(pickIntent, "Select Picture"), 1)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            val selectedImage: Uri? = data.data
            Log.d("TAG", "onActivityResult: $selectedImage")
            val splittedArray=selectedImage.toString().split("%")
            Log.d("TAG", "onActivityResult: ${splittedArray.toString()}")
//            Log.d(TAG, "onActivityResult: ${splittedArray.get(1).toString()}")
            Toast.makeText(this, splittedArray.get(0).substring(2).toString(), Toast.LENGTH_SHORT).show()
            lifecycleScope.launch {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
//                    convertData(selectedImage!!)
                    Log.d("TAG", "onActivityResult: $selectedImage")
                    Toast.makeText(this@VaultActivity, "\n" + selectedImage, Toast.LENGTH_SHORT).show()
                }else{
                    val path= RealPathUtill.getRealPath(this@VaultActivity,selectedImage!!)
                    Toast.makeText(this@VaultActivity, "\n" + path, Toast.LENGTH_SHORT).show()
                }
                val path= getPDFPath(selectedImage!!)
                 FileUtils.copy(path,this@VaultActivity.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.path)
            }


        }
    }
   /* @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // For loading Image
        if (resultCode != RESULT_CANCELED) {
            when (requestCode) {
                0 -> if (resultCode == RESULT_OK && data != null) {
                    val imageSelected = data.extras!!["data"] as Bitmap?
                    binding.imageView.setImageBitmap(imageSelected)
                }
                1 -> if (resultCode == RESULT_OK && data != null) {
                    val imageSelected = data.data
                    val pathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    if (imageSelected != null) {
                        val myCursor = contentResolver.query(
                            imageSelected,
                            pathColumn, null, null, null
                        )
                        if (myCursor != null) {
                            myCursor.moveToFirst()
                            val columnIndex = myCursor.getColumnIndex(pathColumn[0])
                            val picturePath = myCursor.getString(columnIndex)
                            binding.imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath))
                            myCursor.close()
                        }
                    }
                }
            }
        }

        // For loading PDF
        when (requestCode) {
            12 -> if (resultCode == RESULT_OK) {

                pdfUri = data?.data!!
                val uri: Uri = data?.data!!
                val uriString: String = uri.toString()
                var pdfName: String? = null
                if (uriString.startsWith("content://")) {
                    var myCursor: Cursor? = null
                    try {
                        myCursor =
                            applicationContext!!.contentResolver.query(uri, null, null, null, null)
                        if (myCursor != null && myCursor.moveToFirst()) {
                            pdfName = myCursor.getString(
                                myCursor.getColumnIndex(
                                    OpenableColumns.DISPLAY_NAME
                                )
                            )
                            binding.selectedPdf.text = pdfName
                        }
                    } finally {
                        myCursor?.close()
                    }
                }
            }
        }

    }*/
   open fun getPDFPath(uri: Uri?): String? {
       val id = DocumentsContract.getDocumentId(uri)
       val contentUri = ContentUris.withAppendedId(
           Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
       )
       val projection = arrayOf(MediaStore.Images.Media.DATA)
       val cursor = contentResolver.query(contentUri, projection, null, null, null)
       val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
       cursor!!.moveToFirst()
       return cursor!!.getString(column_index)
   }

}