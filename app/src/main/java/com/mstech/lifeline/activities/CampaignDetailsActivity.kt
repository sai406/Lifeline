package com.mstech.lifeline.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.SPStaticUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.mstech.lifeline.models.SharedKey
import com.mstech.lifeline.R

import com.mstech.lifeline.api.RetrofitApi
import com.mstech.lifeline.databinding.ActivityCampaignDetailsBinding
import com.mstech.lifeline.models.NewCampaignsItem
import com.mstech.lifeline.utils.Utils
import kotlinx.coroutines.launch
import java.util.*

class CampaignDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityCampaignDetailsBinding
    var campid: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCampaignDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.includeTop.ivBack.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        binding.includeTop.tvHeader.setText("Campaign Details")
        var data = intent?.getParcelableExtra<NewCampaignsItem?>("details")
        var type = intent?.getStringExtra("type")
        binding.campaignaddress.text =  data?.geoLocation
        binding.campaignname.text = data?.campaignTitle
        binding.starttime.text = data?.datestring
        binding.specialInstructions.text = data?.specialInstructions
        campid = data?.campaignId.toString()
        SPStaticUtils.put(SharedKey.CAMPID, data?.campaignId.toString())
        Glide.with(this)  //2
            .load(data?.imagePath) //3
            .placeholder(R.drawable.ic_loading) //5
            .error(R.drawable.logo) //6
            .fallback(R.drawable.logo) //7
            .into(binding.campaignimage)
        if (!type.equals("1")) {
            binding.sign.visibility = View.GONE
            binding.msg.visibility = View.GONE
        }
        binding.map.setOnClickListener {
            try {
                val uri = String.format(
                    Locale.ENGLISH,
                    "http://maps.google.com/maps?q=loc:%f,%f",
                    data?.latitude,
                    data?.longitude
                )
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
    }
//        binding.start.setOnClickListener(View.OnClickListener {
//            binding.start.visibility = View.GONE
//            binding.stop.visibility = View.VISIBLE
//            if (!ServiceUtils.isServiceRunning(TrackingService::class.java)) {
//                ServiceUtils.startService(TrackingService::class.java)
//            }
//        })
//        binding.stop.setOnClickListener(View.OnClickListener {
//            binding.start.visibility = View.VISIBLE
//            binding.stop.visibility = View.GONE
//            if (ServiceUtils.isServiceRunning(TrackingService::class.java)) {
//                ServiceUtils.stopService(TrackingService::class.java)
//            }
//        })
    binding.sign.setOnClickListener(View.OnClickListener
    {
        lifecycleScope.launch {
            getCampains()
        }
    })
}

private suspend fun getCampains() {
    Utils.showProgress(this, true)
    val response = RetrofitApi().signedCampain(
        SPStaticUtils.getString(SharedKey.CUSTOMER_ID, "0"),
        campid,
        binding.msg.text.toString()
    )
    if (response.isSuccessful) {
        ToastUtils.showShort(response.body()?.statusMessage)
        onBackPressed()
    } else {
        ToastUtils.showShort(response.errorBody().toString())
    }
    Utils.showProgress(this, false)
}
}