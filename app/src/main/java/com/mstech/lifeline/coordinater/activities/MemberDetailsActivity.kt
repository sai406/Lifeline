package com.mstech.lifeline.coordinater.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.mstech.lifeline.databinding.ActivityMemberDetailsBinding
import layout.CoordinatorMemberResponse
import java.util.*

class MemberDetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityMemberDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemberDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.includeTop.ivBack.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        binding.includeTop.tvHeader.setText("Member Details")
        if (intent.extras!=null){
            val memberItem = intent?.getParcelableExtra("data") as CoordinatorMemberResponse?
            binding.customerName.text = memberItem?.FirstName+" "+memberItem?.LastName
            Glide.with(this)
                .load(memberItem?.CustomerImagePath) // image url
                .placeholder(android.R.drawable.gallery_thumb) // any placeholder to load at start
                .error(android.R.drawable.stat_notify_error)  // any image in case of error
                .override(200, 200) // resizing
                .centerCrop()
                .into(binding.customerImage)  // imageview object.
            binding.deafultLocation.text = memberItem?.GeoAddress
            binding.email.setText(memberItem?.EmailId)
            binding.mobile.setText(memberItem?.Mobile)
            binding.mobile.setOnClickListener(View.OnClickListener {
                val u: Uri = Uri.parse("tel:${memberItem?.Mobile}")
                val i = Intent(Intent.ACTION_VIEW, u)
                startActivity(i)
            })
            binding.currentLocation.setOnClickListener(
                View.OnClickListener {
                    try {
                        val uri = String.format(
                            Locale.ENGLISH,
                            "http://maps.google.com/maps?q=loc:%f,%f",
                            memberItem?.Latitude,
                            memberItem?.Longitude
                        )
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                        startActivity(intent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            )
        }

    }
}