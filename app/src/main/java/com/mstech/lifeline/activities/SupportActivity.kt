package com.mstech.lifeline.activities

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.SPStaticUtils
import com.blankj.utilcode.util.ToastUtils
import com.mstech.lifeline.models.SharedKey
import com.mstech.lifeline.api.RetrofitApi
import com.mstech.lifeline.databinding.ActivitySupportBinding
import com.mstech.lifeline.utils.Utils
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.lang.Exception


class SupportActivity : AppCompatActivity() {
    lateinit var binding: ActivitySupportBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySupportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.includeTop.tvHeader.text = "Support"
        binding.includeTop.ivBack.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        binding.send.setOnClickListener(View.OnClickListener {
            if (NetworkUtils.isConnected()) {
                lifecycleScope.launch {
                    sendSupport()
                }
            } else {
                ToastUtils.showShort("No Internet connection")
            }
        })
        binding.helplinecall.setOnClickListener(View.OnClickListener {
            startDialActivity(SPStaticUtils.getString(SharedKey.HELPLINENUMBER))
        })
        binding.coordinatorcall.setOnClickListener(View.OnClickListener {
            startDialActivity(SPStaticUtils.getString(SharedKey.COORDINATERNUMBER))
        })

        binding.voicecall.setOnClickListener(View.OnClickListener {
            startDialActivity("12345678910")
        })
        binding.videocall.setOnClickListener(View.OnClickListener {
   /*         if (isAppInstalled(this, "com.google.android.apps.tachyon")) {
                val intent = Intent()
                intent.setPackage("com.google.android.apps.tachyon")
                intent.action = "com.google.android.apps.tachyon.action.CALL"
                intent.data = Uri.parse("tel:8686863442")
                startActivity(intent)
            } else {
                val appPackageName =
                    "com.google.android.apps.tachyon" // getPackageName() from Context or Activity object

                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=$appPackageName")
                        )
                    )
                } catch (anfe: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                        )
                    )
                }
            }*/

            startDialActivity("12345678910")

        })


    }

    fun isAppInstalled(context: Context, packageName: String?): Boolean {
        return try {
            if (packageName != null) {
                context.packageManager.getApplicationInfo(packageName, 0)
            }
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun startDialActivity(phone: String) {
        try {
            val intent = Intent("com.android.phone.videocall")
            intent.putExtra("videoCall", true)
            intent.data = Uri.parse("tel:$phone")
            startActivity(intent)
        } catch (e: Exception) {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phone")
            startActivity(intent)
        }

    }

    private suspend fun sendSupport() {
        Utils.showProgress(this, true)
        var obj = JSONObject()
        obj.put("MemberId", SPStaticUtils.getString(SharedKey.CUSTOMER_ID))
        obj.put("Subject", binding.title.text.toString())
        obj.put("Message", binding.message.text.toString())
        var finalbody = ((obj)).toString()
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val response = RetrofitApi().supportMessage(finalbody)
        if (response.isSuccessful) {
            var obj = JSONObject(response.body()?.string())
            ToastUtils.showShort(obj.getString("StatusMessage"))
            onBackPressed()
        } else {
            ToastUtils.showShort(response.errorBody()?.string())
        }
        Utils.showProgress(this, false)
    }
}