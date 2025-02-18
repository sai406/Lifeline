package com.mstech.lifeline.resources.activities

import android.R.attr
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mstech.lifeline.databinding.ActivityResourceDetailsBinding
import android.R.attr.data
import android.content.Intent
import android.view.View
import com.mstech.lifeline.resources.model.ResourceResponse
import com.mstech.lifeline.utils.WebViewWithNavigation


class ResourceDetailsActivity : AppCompatActivity() {
    
    lateinit var binding: ActivityResourceDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResourceDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.includeTop.ivBack.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        binding.includeTop.tvHeader.setText("Resource Details")
        var resourceitem = intent.getParcelableExtra("data") as ResourceResponse?
        binding.title.text = resourceitem?.DocTitle.toString()
        binding.desc.text = resourceitem?.ResourceBrief.toString()
        binding.document.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,WebViewWithNavigation::class.java).putExtra("url",resourceitem?.ResourceFilePath))
        })

    }
}