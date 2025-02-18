package com.mstech.lifeline.coordinater.activities

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.SPStaticUtils
import com.blankj.utilcode.util.ToastUtils
import com.mstech.lifeline.models.SharedKey
import com.mstech.lifeline.R
import com.mstech.lifeline.api.RetrofitApi
import com.mstech.lifeline.coordinater.adapters.SOSAdapter
import com.mstech.lifeline.coordinater.adapters.SOSUnnoticeAdapter
import com.mstech.lifeline.coordinater.model.LatestItem
import com.mstech.lifeline.coordinater.model.NoticedItem
import com.mstech.lifeline.coordinater.model.RescueAction
import com.mstech.lifeline.databinding.ActivityIncidentListBinding
import com.mstech.lifeline.utils.Utils
import kotlinx.coroutines.launch

class IncidentListActivity : AppCompatActivity(), RescueAction {
    lateinit var binding: ActivityIncidentListBinding
    lateinit var noticelist: List<NoticedItem>
    lateinit var unnoticelist: List<LatestItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIncidentListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.includeTop.ivBack.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        binding.includeTop.tvHeader.setText("Incidents List")
        binding.noticeRecyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.unnoticeRecyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.risk.setBackgroundColor(resources.getColor(R.color.appred))
        binding.risk.setTextColor(Color.WHITE)
        binding.notice.setBackgroundColor(Color.WHITE)
        binding.notice.setTextColor(Color.BLACK)
        binding.unnoticeRecyclerView.visibility = View.VISIBLE
        binding.noticeRecyclerView.visibility = View.GONE
        binding.risk.setOnClickListener(View.OnClickListener {
            binding.risk.setBackgroundColor(resources.getColor(R.color.appred))
            binding.risk.setTextColor(Color.WHITE)
            binding.notice.setBackgroundColor(Color.WHITE)
            binding.notice.setTextColor(Color.BLACK)
            binding.unnoticeRecyclerView.visibility = View.VISIBLE
            binding.noticeRecyclerView.visibility = View.GONE
        })
        binding.notice.setOnClickListener(View.OnClickListener {
            binding.risk.setBackgroundColor(Color.WHITE)
            binding.risk.setTextColor(Color.BLACK)
            binding.notice.setBackgroundColor(resources.getColor(R.color.appred))
            binding.notice.setTextColor(Color.WHITE)
            binding.unnoticeRecyclerView.visibility = View.GONE
            binding.noticeRecyclerView.visibility = View.VISIBLE
        })

    }

    override fun onResume() {

        super.onResume()
        lifecycleScope.launch {
            getsos()
        }
    }

    private suspend fun getsos() {
        Utils.showProgress(this, true)
        val response = RetrofitApi().getIncidentList(SPStaticUtils.getString(SharedKey.CUSTOMER_ID))
        if (response.isSuccessful) {
            noticelist = response.body()?.noticed as List<NoticedItem>
            unnoticelist = response.body()?.latest as List<LatestItem>
            binding.noticeRecyclerView.adapter = SOSAdapter(
                this,
                noticelist
            )
            binding.unnoticeRecyclerView.adapter = SOSUnnoticeAdapter(
                this,
                unnoticelist, this
            )
        } else {
            ToastUtils.showShort(response.errorBody()?.string())
        }
        Utils.showProgress(this, false)
    }

    public suspend fun rescueAction(helpId: Int) {
        Utils.showProgress(this, true)
        var response = RetrofitApi().rescueButton(helpId.toString(), 1)
        if (response.isSuccessful) {
            var data = (response.body())
            if (data?.statusCode!! > 0) {
                ToastUtils.showShort("Successfull")
            } else {
                ToastUtils.showShort("Try Again")
            }
        } else {
            ToastUtils.showShort(response.errorBody()?.string())
        }
        Utils.showProgress(this, false)
    }

    override fun rescueBtn(helpId: Int) {
        lifecycleScope.launch {
            rescueAction(helpId)
        }
    }
}