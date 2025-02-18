package com.mstech.lifeline.activities

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.SPStaticUtils
import com.blankj.utilcode.util.ToastUtils
import com.mstech.lifeline.models.SharedKey
import com.mstech.lifeline.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    var address: String = ""
    var lat: String = ""
    var lon: String = ""
    var postal: String = ""

    private val ALL_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.RECORD_AUDIO,
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        if (!PermissionUtils.isGranted(*ALL_PERMISSIONS)) {
            PermissionUtils
                .permission(*ALL_PERMISSIONS)
                .callback(object : PermissionUtils.SimpleCallback {
                    override fun onGranted() {
//                        getGps()
                        lifecycleScope.launch {
                            delay(2000)
                            if (SPStaticUtils.getBoolean(SharedKey.ISLOGIN)) {
                                startActivity(
                                    Intent(
                                        this@SplashActivity,
                                        DashboardActivity::class.java
                                    )
                                )
                            } else {
                                startActivity(
                                    Intent(
                                        this@SplashActivity,
                                        LoginActivity::class.java
                                    )
                                )

                            }
                        }
                    }

                    override fun onDenied() {
                        ToastUtils.showShort("Accept all permissions to access app")
                    }
                }).request()
        } else {
//            getGps()
            lifecycleScope.launch {
                delay(2000)

                if (SPStaticUtils.getBoolean(SharedKey.ISLOGIN)) {
                    startActivity(Intent(this@SplashActivity, DashboardActivity::class.java))
                } else {
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))

                }
            }
        }


    }

}
