package com.mstech.lifeline.coordinater.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.mstech.lifeline.api.RetrofitApi
import com.mstech.lifeline.coordinater.adapters.AssignedVolunteersAdapter
import com.mstech.lifeline.databinding.ActivityIncidentDetailsBinding
import com.mstech.lifeline.utils.Utils
import com.mstech.lifeline.utils.WebViewWithNavigation
import kotlinx.coroutines.launch
import java.util.*


class UnnoticedIncidentDetailsActivity : AppCompatActivity() {
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

        lifecycleScope.launch {
            getIncidentDetails(id)
        }
        binding.assign.visibility = View.GONE
        lifecycleScope.launch {
            getAssignedVolunteers(id)
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
                intent.setDataAndType(Uri.parse(data.Videos?.get(0)?.VideoName), "video/*")
                startActivity(intent)
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


}