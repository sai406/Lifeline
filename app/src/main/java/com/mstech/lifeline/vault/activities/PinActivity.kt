package com.mstech.lifeline.vault.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.SPStaticUtils
import com.blankj.utilcode.util.ToastUtils
import com.mstech.lifeline.models.SharedKey
import com.mstech.lifeline.databinding.ActivityPinBinding

class PinActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPinBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.includeTop.ivBack.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        binding.includeTop.tvHeader.setText("Login Pin")
        binding.login.setOnClickListener(View.OnClickListener {
            if (!binding.password.text.toString().equals(SPStaticUtils.getString(SharedKey.OTP))) {
                ToastUtils.showShort("Enter Valid Pin")
            } else {
                startActivity(Intent(this, DocListActivity::class.java))
            }
        })


    }
}