package com.mstech.lifeline.coordinater.activities

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.SPStaticUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.mstech.lifeline.models.SharedKey
import com.mstech.lifeline.api.RetrofitApi
import com.mstech.lifeline.coordinater.adapters.AssignedVolunteersAdapter
import com.mstech.lifeline.databinding.ActivityIncidentDetailsBinding
import com.mstech.lifeline.utils.Utils
import com.mstech.lifeline.utils.WebViewWithNavigation
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.*


class IncidentDetailsActivity : AppCompatActivity() {
    private var id = "0"
    private var type = "0"
    var fromPush = false
    lateinit var binding: ActivityIncidentDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIncidentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.includeTop.tvHeader.text = "Incident Details"
        binding.includeTop.ivBack.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        if (intent.extras != null) {
            id = intent!!.extras!!.getString("helpid").toString()
            type = intent!!.extras!!.getString("from").toString()
        }
        binding.volunteersList.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )

        try {
            val intent = intent
            val uri = intent.data
            if (uri != null) {
                Log.e("URL DeepLink: ", Objects.requireNonNull(uri).toString())
                if (uri.toString().contains("id=") && uri.toString().contains("_")) {
                    id =
                        uri.toString().split("_")[1]
                } else if (uri.toString().contains("id=") && uri.toString().contains("@")) {
                    id =
                        uri.toString().split("@")[1]
                }
                fromPush = true
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        lifecycleScope.launch {
            getIncidentDetails(id)
        }

        if (SPStaticUtils.getString(SharedKey.ISVOLUNTEER).equals("2")) {
            binding.assignLayout.visibility = View.GONE
            binding.assign.text = "Assign to Volunteer?"
        } else if (SPStaticUtils.getString(SharedKey.ISVOLUNTEER).equals("1")) {
            binding.assignLayout.visibility = View.GONE
            binding.assign.text = "Take On"
        } else {
            binding.assign.visibility = View.GONE
            lifecycleScope.launch {
                getAssignedVolunteers(id)
            }
        }
    }

    private suspend fun getAssignedVolunteers(id: String) {
        val response = RetrofitApi().assignedVolunteers(id)
        if (response.isSuccessful) {
            binding.volunteersList.adapter = response.body()?.let {
                AssignedVolunteersAdapter(
                    this,
                    it
                )
            }
        }
    }


    private suspend fun getIncidentDetails(id: String) {
        Utils.showProgress(this, true)
        val response = RetrofitApi().getIncidentDetails(id)
        if (response.isSuccessful) {
            var data = response.body()
            binding.customerName.text = data?.Member?.FirstName
            Glide.with(this)
                .load(data?.Member?.CustomerImagePath) // image url
                .placeholder(android.R.drawable.gallery_thumb) // any placeholder to load at start
                .error(android.R.drawable.stat_notify_error)  // any image in case of error
                .override(200, 200) // resizing
                .centerCrop()
                .into(binding.customerImage)  // imageview object.
            binding.deafultLocation.text = data?.Member?.GeoAddress
            if (!data?.Images!!.isEmpty()) {
                binding.imageLayout.visibility = View.VISIBLE
                Glide.with(this)
                    .load(data.Images?.get(0)?.ImagePath) // image url
                    .placeholder(android.R.drawable.gallery_thumb) // any placeholder to load at start
                    .error(android.R.drawable.stat_notify_error)  // any image in case of error
                    .override(200, 200) // resizing
                    .centerCrop()
                    .into(binding.incidentImage)
                binding.imageLayout.setOnClickListener(View.OnClickListener {
                    startActivity(
                        Intent(this, WebViewWithNavigation::class.java).putExtra(
                            "url",
                            data.Images?.get(0)?.ImagePath
                        )
                    )

                })
            } else {
                binding.imageLayout.visibility = View.GONE
            }
            if (data.Videos!!.size > 0) {
                binding.videoLayout.visibility = View.VISIBLE
            } else {
                binding.videoLayout.visibility = View.GONE
            }
            binding.videoLayout.setOnClickListener(View.OnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(Uri.parse(data.Videos?.get(0)?.VideoPath), "video/*")
                startActivity(intent)
            })
            binding.assign.setOnClickListener(View.OnClickListener {
                if (SPStaticUtils.getString(SharedKey.ISVOLUNTEER).equals("2")) {
                    val alertDialogBuilder: android.app.AlertDialog.Builder =
                        android.app.AlertDialog.Builder(this)
                    alertDialogBuilder.setTitle("Confirmation")
                    alertDialogBuilder
                        .setMessage("Assign to Volunteer!")
                        .setCancelable(false)
                        .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, idd ->
                            lifecycleScope.launch {
                                rescueAction(id)
                            }

                        })
                        .setNegativeButton(
                            "No",
                            DialogInterface.OnClickListener { dialog, id -> // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel()
                            })
                    val alertDialog: android.app.AlertDialog? = alertDialogBuilder.create()
                    alertDialog!!.show()
                } else if (type == "2") {

                } else if (SPStaticUtils.getString(SharedKey.ISVOLUNTEER).equals("1")) {

                    val alertDialog: android.app.AlertDialog? =
                        android.app.AlertDialog.Builder(this).create()
                    alertDialog!!.setTitle("Respond")
                    alertDialog.setMessage("Are you Accept?")
//                    alertDialog.setButton(
//                        AlertDialog.BUTTON_NEGATIVE, "Decline",
//                        DialogInterface.OnClickListener { dialog, which ->
//                            lifecycleScope.launch {
//                                rescued(id.toString(), 0)
//                            }
//                        })
                    alertDialog.setButton(
                        AlertDialog.BUTTON_POSITIVE, "Accept",
                        DialogInterface.OnClickListener { dialog, which ->
                            lifecycleScope.launch {
                                rescued(id.toString(), 1)
                            }

                        })
                    alertDialog.setButton(
                        AlertDialog.BUTTON_NEGATIVE, "Cancel",
                        DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
                    alertDialog.show()

                }
            })
            binding.incidentLocation.setOnClickListener(View.OnClickListener {
                try {
                    val uri = String.format(
                        Locale.ENGLISH,
                        "http://maps.google.com/maps?q=loc:%f,%f",
                        data.Member?.Latitude,
                        data.Member?.Longitude
                    )
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            })


        } else {
            ToastUtils.showShort(response.errorBody()?.string())
        }
        Utils.showProgress(this, false)
    }

    suspend fun rescueAction(helpId: String) {
        Utils.showProgress(this, true)
        var response = RetrofitApi().rescueButton(helpId, 1)
        if (response.isSuccessful) {
            var data = (response.body())
            if (data?.statusCode!! > 0) {
                ToastUtils.showShort("Successfull")
                onBackPressed()
            } else {
                ToastUtils.showShort("Try Again")
            }
        } else {
            ToastUtils.showShort(response.errorBody()?.string())
        }
        Utils.showProgress(this, false)
    }

    private suspend fun rescued(id: String, status: Int) {
        Utils.showProgress(this, true)
        var obj = JSONObject()
        obj.put("MemberId", SPStaticUtils.getString(SharedKey.CUSTOMER_ID, ""))
        obj.put("HelpId", id)
        obj.put("IsAccepted", status)
        var finalbody = ((obj)).toString()
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val response = RetrofitApi().rescueAction(finalbody)
        if (response.isSuccessful) {

//            if (response.body().RespondId==0){
//                ToastUtils.showShort(response.body()?.statusMessage)
//            }else{
//                ToastUtils.showShort(response.body()?.statusMessage)
//            }

//            if (response.body()?.statusCode!! >= 0) {
            ToastUtils.showLong(response.body()?.Message)
            startActivity(Intent(this,IncidentListActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
            finish()
//            } else {
//                ToastUtils.showShort(response.body()?.statusMessage)
//            }

        } else {
            ToastUtils.showShort(response.errorBody()?.string())
        }
        Utils.showProgress(this, false)
    }
}