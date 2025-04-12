package com.mstech.lifeline.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.SPStaticUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.mstech.lifeline.models.SharedKey
import com.mstech.lifeline.api.RetrofitApi
import com.mstech.lifeline.databinding.ActivityLoginBinding
import com.mstech.lifeline.utils.Utils.showProgress
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var token: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        supportActionBar?.setTitle("Login")
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.login.setOnClickListener(View.OnClickListener {
            if (!NetworkUtils.isConnected()) {
                ToastUtils.showShort("No Internet Connection")
            } else if (binding.username.text!!.length < 3) {
                ToastUtils.showShort("Enter Username")
            } else if (binding.password.text!!.length < 3) {
                ToastUtils.showShort("EnterPassword")
            } else {
                lifecycleScope.launch {
                    KeyboardUtils.hideSoftInput(this@LoginActivity)
                    loginUser()
                }
            }
        })
        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this,SignupActivity::class.java))
        }
//        FirebaseInstanceIdReceiver.().instanceId
//            .addOnCompleteListener(OnCompleteListener { task ->
//                if (!task.isSuccessful) {
//                    Log.w("TAG", "getInstanceId failed", task.exception)
//                    return@OnCompleteListener
//                }
//
//                // Get new Instance ID token
//
//
//                // Log and toast
//                Log.d("TAG", token)
//            })


        FirebaseMessaging.getInstance().token.addOnCompleteListener { task: Task<String> ->
            if (!task.isSuccessful) {
                return@addOnCompleteListener
            }

            token = task.result
            Log.i("PUSH_TOKEN", "pushToken: $token")
        }
    }

    data class LoginsResponse(
        val ResultId : Int,
        val MemberId : String,
        val ResultMessage : String,
        val IsCoordinator : Int,
    )

    suspend fun loginUser() {
        showProgress(this, true)
        var obj = JSONObject()
        obj.put("UserName", binding.username.text.toString())
        obj.put("Password", binding.password.text.toString())
        var finalbody = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            ((obj)).toString()
        )
        var response =
            RetrofitApi().loginRequest(finalbody)
        if (response.isSuccessful) {
            var data = response.body()!!
            if (data.ResultId!! == 0) {
                ToastUtils.showShort(data.ResultMessage)
            } else {
                ToastUtils.showShort("Login Successfull")
                SPStaticUtils.put(SharedKey.CUSTOMER_ID, data!!.MemberId.toString())
                SPStaticUtils.put(SharedKey.ISLOGIN, true)
                if (data.IsCoordinator == 0){
                    SPStaticUtils.put(SharedKey.ISVOLUNTEER, "0")
                }else{
                    SPStaticUtils.put(SharedKey.ISVOLUNTEER, "2")
                }
                SPStaticUtils.put(SharedKey.HELPLINENUMBER,"")
                SPStaticUtils.put(SharedKey.COORDINATERNUMBER, "")
                SPStaticUtils.put(SharedKey.COUNTRYID, "")
                SPStaticUtils.put(SharedKey.PROFILEPIC, "")
                SPStaticUtils.put(
                    SharedKey.NAME,
                    "" + " "
                )
                SPStaticUtils.put(SharedKey.MOBILE, "")
                SPStaticUtils.put(SharedKey.EMAIL, "")
                SPStaticUtils.put(SharedKey.OTP, "")
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            }
        } else {
            ToastUtils.showShort(response.errorBody().toString())
        }
        showProgress(this, false)
    }

}