package com.mstech.lifeline.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.SPStaticUtils
import com.blankj.utilcode.util.ToastUtils
import com.mstech.lifeline.R
import com.mstech.lifeline.api.RetrofitApi
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        lifecycleScope.launch {
            sentSos()
        }

    }
    private suspend fun sentSos() {

        var obj = JSONObject()
        obj.put("email", "saikumarbadapatla@gmail.com")

        var finalbody = ((obj)).toString()
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val response = RetrofitApi().sentSos(finalbody)
        if (response.isSuccessful) {

            Log.d("TAG", "sentSos: "+response.body()?.string())
            ToastUtils.showShort("Sent Successfull")

        } else {
            ToastUtils.showShort(response.errorBody()?.string())

        }


    }
}