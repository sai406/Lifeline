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

    suspend fun loginUser() {
        showProgress(this, true)
        var response =
            RetrofitApi().login(
                binding.username.text.toString(),
                binding.password.text.toString(),
                token,
                "1"
            )
        if (response.isSuccessful) {
            var data = response.body()!!
            if (data.result?.statusCode!! < 0) {
                ToastUtils.showShort(data.result?.statusMessage)
            } else {
                ToastUtils.showShort("Login Successfull")
                SPStaticUtils.put(SharedKey.CUSTOMER_ID, data.details?.memberId!!.toString())
                SPStaticUtils.put(SharedKey.ISLOGIN, true)
                SPStaticUtils.put(SharedKey.ISVOLUNTEER, data.details.isCoordinator.toString())
                SPStaticUtils.put(SharedKey.HELPLINENUMBER, data.details.helpLineNumber)
                SPStaticUtils.put(SharedKey.COORDINATERNUMBER, data.details.coordinatorNumber)
                SPStaticUtils.put(SharedKey.COUNTRYID, data.details.countryId.toString())
                SPStaticUtils.put(SharedKey.PROFILEPIC, data.details.customerImagePath?:"")
                SPStaticUtils.put(
                    SharedKey.NAME,
                    data.details.firstName.toString() + " " + data.details.lastName.toString()
                )
                SPStaticUtils.put(SharedKey.MOBILE, data.details.mobile.toString())
                SPStaticUtils.put(SharedKey.EMAIL, data.details.emailId.toString())
                SPStaticUtils.put(SharedKey.OTP, data.details.pin.toString())
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            }
        } else {
            ToastUtils.showShort(response.errorBody().toString())
        }
        showProgress(this, false)
    }

}