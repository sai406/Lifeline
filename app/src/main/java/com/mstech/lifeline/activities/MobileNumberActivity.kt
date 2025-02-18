package com.mstech.lifeline.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.SPStaticUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.mstech.lifeline.models.SharedKey
import com.mstech.lifeline.R
import com.mstech.lifeline.api.RetrofitApi
import com.mstech.lifeline.databinding.ActivityMobileNumberBinding
import com.mstech.lifeline.utils.Utils.showProgress
import kotlinx.coroutines.launch
import org.json.JSONException
import java.util.*

class MobileNumberActivity : AppCompatActivity() {
    lateinit var binding: ActivityMobileNumberBinding
    var otp:String=""
    var userid:String=""
    var token:String=""
    var countryList: ArrayList<String> = ArrayList<String>()
    var countryIdList: ArrayList<String> = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMobileNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)
    getCountries()
        binding.getotp.setOnClickListener(View.OnClickListener {
            if (binding.etPhone.text.length>4) {
                lifecycleScope.launch {
                    if (NetworkUtils.isConnected()) {
                        getVerificationcode(binding.countryspinner.selectedItem.toString()+binding.etPhone.text.toString())
                    } else {
                        ToastUtils.showShort("No Internet Connection")
                    }
                }

            } else {
                ToastUtils.showShort("Not a Valid Number")
            }
        })
        binding.submit.setOnClickListener(View.OnClickListener {
            if (binding.otp.text.toString().equals(otp)) {
                if (userid.equals("0")) {
                    SPStaticUtils.put(SharedKey.MOBILE, binding.countryspinner.selectedItem.toString()+binding.etPhone.text.toString())
                    ToastUtils.showShort("Verification Successfull")
                    SPStaticUtils.put(SharedKey.COUNTRYID,countryIdList.get(binding.countryspinner.selectedItemPosition))
                    startActivity(Intent(this, UserdetailsActivity::class.java))
                } else {
                    SPStaticUtils.put(SharedKey.MOBILE, binding.countryspinner.selectedItem.toString()+binding.etPhone.text.toString())
                    SPStaticUtils.put(SharedKey.ISLOGIN, true)
                    SPStaticUtils.put(SharedKey.COUNTRYID,countryIdList.get(binding.countryspinner.selectedItemPosition))
                    SPStaticUtils.put(SharedKey.CUSTOMER_ID, userid)
                    ToastUtils.showShort("Verification Successfull")
                    startActivity(Intent(this, DashboardActivity::class.java))
                }

            }
        })
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task: Task<String> ->
            if (!task.isSuccessful) {
                return@addOnCompleteListener
            }

            token = task.result
            Log.i("PUSH_TOKEN", "pushToken: $token")
        }
    }

    private suspend fun getVerificationcode(fullNumber: String) {
        showProgress(this, true)
        val response = RetrofitApi().getVerificationCode(fullNumber, token, "1")
        showProgress(this, false)
        if (response.isSuccessful){
            otp = response.body()?.statusMessage.toString()
            userid = response.body()?.statusCode.toString()
            SPStaticUtils.put(SharedKey.ISVOLUNTEER, response.body()?.isVolunteer.toString())
            SPStaticUtils.put(SharedKey.HELPLINENUMBER, response.body()?.helpLineNumber)
            SPStaticUtils.put(SharedKey.COORDINATERNUMBER, response.body()?.coordinatorNumber)
            binding.getotp.visibility =View.GONE
            binding.otplayout.visibility =View.VISIBLE
            binding.submit.visibility =View.VISIBLE
        }else{
            ToastUtils.showShort("Error")
        }
    }
    fun getCountries() {
        countryList.clear()
        countryIdList.clear()
        val requestQueue = Volley.newRequestQueue(this)
        val url = "http://civiccare.net/api/GetDdlCountry"
        Log.d("sss", "url:$url")
        val movieReq = JsonArrayRequest(url,
            { response ->
                Log.d("c", response.toString())
                for (i in 0 until response.length()) {
                    try {
                        val obj = response.getJSONObject(i)
                        countryIdList.add(obj.getString("CountryId"))
                        countryList.add(obj.getString("CountryCode"))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    this,
                    R.layout.support_simple_spinner_dropdown_item,
                    countryList
                )
                binding.countryspinner.setAdapter(adapter)
            }) { }
        requestQueue.add(movieReq)
    }
}