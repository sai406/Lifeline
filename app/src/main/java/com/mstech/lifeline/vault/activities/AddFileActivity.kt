package com.mstech.lifeline.vault.activities

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.UriUtils
import com.mstech.lifeline.R
import com.mstech.lifeline.databinding.ActivityAddFileBinding
import com.mstech.lifeline.utils.RealPathUtill
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.security.SecureRandom
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class AddFileActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddFileBinding
    var isPhoto : Boolean = true
    lateinit var selectedDoc : Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (!checkPermissions()) {
            requestPermissions()
        }
        supportActionBar?.hide()
        binding.includeTop.ivBack.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        binding.includeTop.tvHeader.setText("Add Document")
        binding.docImage.setOnClickListener(View.OnClickListener {
            selectDocument()
        })
        binding.saveDocument.setOnClickListener(View.OnClickListener {
            if (binding.documentName.text.toString().isNullOrEmpty()){
                ToastUtils.showShort("Enter Title")
            }else if (selectedDoc == null){
                ToastUtils.showShort("Choose Document")
            } else if(isPhoto){

                lifecycleScope.launch {
                    val path = RealPathUtill.getRealPath(this@AddFileActivity, selectedDoc)
                    var photoFile = createImageFile(binding.documentName.text.toString())
                    FileUtils.copy(
                        path, photoFile?.path
                    )
                    ToastUtils.showShort("Saved Successfully")
                }
                binding.docImage.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_add_24))
                binding.documentName.setText("")
                startActivity(Intent(this,DocListActivity::class.java))
                finish()
            }else if (!isPhoto){
                lifecycleScope.launch {
                    val path = UriUtils.uri2File(selectedDoc!!)
                    var photoFile = createPdfFile(binding.documentName.text.toString())
                    FileUtils.copy(
                        path.absolutePath, photoFile?.path
                    )
                    ToastUtils.showShort("Saved Successfully")
                }
                binding.docImage.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_add_24))
                binding.documentName.setText("")
                startActivity(Intent(this,DocListActivity::class.java))
                finish()
            }
        })
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

    fun imagePicker() {
        val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val pickIntent = Intent(Intent.ACTION_PICK, collection)
        pickIntent.type = "image/*"
        pickIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(pickIntent, 1)
    }

    fun pdfPicker() {
        val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
        pdfIntent.type = "application/pdf"
        pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(pdfIntent, 2)
    }

    private fun selectDocument() {
        // Creating AlertDialog
        val choice = arrayOf<CharSequence>("Photo", "Pdf", "Cancel")
        val myAlertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        myAlertDialog.setTitle("Select Document type")
        myAlertDialog.setItems(choice, DialogInterface.OnClickListener { dialog, item ->
            when {
                choice[item] == "Photo" -> {
                    imagePicker()
                }
                choice[item] == "Pdf" -> {
                    pdfPicker()
                }
                choice[item] == "Cancel" -> {
//                            myAlertDialog.()
                }
            }
        })
        myAlertDialog.show()
    }

    @Throws(IOException::class)
    fun createImageFile(title : String): File {
        // Create an image file name
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            title+ " "+getDateTime()+"_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
        }
    }

    @Throws(IOException::class)
    fun createPdfFile(title :String): File {
        // Create an image file name
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        return File.createTempFile(
            title + " " +getDateTime()+"_", /* prefix */
            ".pdf", /* suffix */
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
             selectedDoc = data.data!!
            isPhoto = true
            binding.docImage.setImageURI(selectedDoc)

        } else if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
             selectedDoc = data.data!!
            isPhoto = false
            binding.docImage.setImageDrawable(getResources().getDrawable(R.drawable.pdficon));
        }
    }

}