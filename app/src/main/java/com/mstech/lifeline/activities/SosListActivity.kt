package com.mstech.lifeline.activities

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.SPStaticUtils
import com.blankj.utilcode.util.ToastUtils
import com.mstech.lifeline.models.SharedKey
import com.mstech.lifeline.adapter.SOSAdapter
import com.mstech.lifeline.api.RetrofitApi
import com.mstech.lifeline.databinding.ActivitySosListBinding
import com.mstech.lifeline.models.SosInterface
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject

class SosListActivity : BaseActivity(), SosInterface {
    lateinit var binding: ActivitySosListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySosListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.includeTop.ivBack.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        binding.includeTop.tvHeader.setText("Incident List")
        binding.recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        lifecycleScope.launch {
            getsos()
        }
    }

    private suspend fun getsos() {
        showPDialog("Pleasewait ..")
        val response = RetrofitApi().getSos(SPStaticUtils.getString(SharedKey.CUSTOMER_ID,""))
        if (response.isSuccessful) {
            binding.recyclerView.adapter = response.body()?.let {
                SOSAdapter(
                    this,
                    it, this
                )
            }
        } else {
            ToastUtils.showShort(response.errorBody()?.string())
        }
        hidePDialog()
    }

    private suspend fun rescued(id: Int, status: Int ) {
        showPDialog("Pleasewait ..")
        var obj = JSONObject()
        obj.put("MemberId", SPStaticUtils.getString(SharedKey.CUSTOMER_ID,""))
        obj.put("HelpId", id.toString())
        obj.put("IsAccepted", status.toString())
        var finalbody = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            ((obj)).toString()
        )
        val response = RetrofitApi().rescueAction(finalbody)
        if (response.isSuccessful) {
//            if (response.body()?.statusCode!! >= 0) {
                ToastUtils.showShort("Responded")
                onBackPressed()
//            } else {
//                ToastUtils.showShort(response.body()?.statusMessage)
//            }

        } else {
            ToastUtils.showShort(response.errorBody()?.string())
        }
        hidePDialog()
    }


    override fun onClicked(id: Int , status : Int) {
        lifecycleScope.launch {
            rescued(id,status)
        }
    }
}