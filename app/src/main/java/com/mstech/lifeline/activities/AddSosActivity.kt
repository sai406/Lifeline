package com.mstech.lifeline.activities

import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.SPStaticUtils
import com.blankj.utilcode.util.ToastUtils
import com.mstech.lifeline.models.SharedKey
import com.mstech.lifeline.api.RetrofitApi
import com.mstech.lifeline.databinding.ActivityAddSosBinding
import com.mstech.lifeline.utils.Utils
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
import java.util.regex.Pattern

class AddSosActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddSosBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.includeTop.ivBack.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        binding.includeTop.tvHeader.setText("Add SOS")
        binding.submit.setOnClickListener(View.OnClickListener {
            if (!NetworkUtils.isConnected()) {
                ToastUtils.showShort("No Internet Connection")
            } else if (binding.sosname.text.isNullOrBlank()) {
                ToastUtils.showShort("Enter Sos Contact Name")
            } else if (binding.sosmobile.text.isNullOrBlank()) {
                ToastUtils.showShort("Enter Sos Mobile")
            } else {
                lifecycleScope.launch {
                    addSos()
                }
            }
        })
    }


    private suspend fun addSos() {
        Utils.showProgress(this, true)
        var obj = JSONObject()
        obj.put("MemberId", SPStaticUtils.getString(SharedKey.CUSTOMER_ID))
        obj.put("Name", binding.sosname.text.toString())
        obj.put("EmailId", binding.sosemail.text.toString())
        obj.put("Mobile", binding.sosmobile.text.toString())
        var finalbody = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            ((obj)).toString()
        )
        val response = RetrofitApi().addSOS(finalbody)
        if (response.isSuccessful) {

//            startActivity(Intent(this,DashboardActivity::class.java))
//            if (response.body()?.statusCode!!>0){
//                SPStaticUtils.put(SharedKey.CUSTOMER_ID,response.body()?.statusCode.toString())
//                SPStaticUtils.put(SharedKey.ISLOGIN,true)
//                ToastUtils.showShort("Signup Successfull")
//            }else{
            ToastUtils.showShort("Saved Succesfully")
            onBackPressed()
//            }
        } else {
            ToastUtils.showShort(response.errorBody()?.string())
        }
        Utils.showProgress(this, false)
    }

    private fun isValidEmail(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }
}