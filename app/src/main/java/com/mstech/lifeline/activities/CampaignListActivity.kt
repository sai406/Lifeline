package com.mstech.lifeline.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.SPStaticUtils
import com.blankj.utilcode.util.ToastUtils
import com.mstech.lifeline.models.SharedKey
import com.mstech.lifeline.R
import com.mstech.lifeline.adapter.CampainListAdapter
import com.mstech.lifeline.api.RetrofitApi
import com.mstech.lifeline.databinding.ActivityCampaignListBinding
import com.mstech.lifeline.models.CampaignResponse
import com.mstech.lifeline.models.NewCampaignsItem
import com.mstech.lifeline.utils.Utils.showProgress
import kotlinx.coroutines.launch

class CampaignListActivity : AppCompatActivity() {
    lateinit var binding: ActivityCampaignListBinding
    var campaignlist = ArrayList<NewCampaignsItem>()
    var signcampaignlist = ArrayList<NewCampaignsItem>()
    var donecampaignlist = ArrayList<NewCampaignsItem>()
    var adapter: CampainListAdapter? = null
    lateinit var data: CampaignResponse
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCampaignListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.includeTop.ivBack.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        binding.includeTop.tvHeader.setText("Campaign List")
        val linearLayoutManager = LinearLayoutManager(this)
        binding.recyclerView?.layoutManager = linearLayoutManager

        binding.open.setOnClickListener(View.OnClickListener {
            binding.open.setBackgroundColor(resources.getColor(R.color.appred))
            binding.signed.setBackgroundColor(resources.getColor(R.color.quantum_grey))
            binding.done.setBackgroundColor(resources.getColor(R.color.quantum_grey))

            lifecycleScope.launch {
                getCampains("1")
            }


        })
        binding.signed.setOnClickListener(View.OnClickListener {
            binding.signed.setBackgroundColor(resources.getColor(R.color.appred))
            binding.open.setBackgroundColor(resources.getColor(R.color.quantum_grey))
            binding.done.setBackgroundColor(resources.getColor(R.color.quantum_grey))

            lifecycleScope.launch {
                getCampains("2")
            }

        })
        binding.done.setOnClickListener(View.OnClickListener {
            binding.done.setBackgroundColor(resources.getColor(R.color.appred))
            binding.signed.setBackgroundColor(resources.getColor(R.color.quantum_grey))
            binding.open.setBackgroundColor(resources.getColor(R.color.quantum_grey))
            lifecycleScope.launch {
                getCampains("3")
            }


        })
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            binding.open.setBackgroundColor(resources.getColor(R.color.appred))
            binding.signed.setBackgroundColor(resources.getColor(R.color.quantum_grey))
            binding.done.setBackgroundColor(resources.getColor(R.color.quantum_grey))
            getCampains("1")
        }
    }

    private suspend fun getCampains(type: String) {
        showProgress(this, true)
        val response = RetrofitApi().getCampaignList(SPStaticUtils.getString(SharedKey.CUSTOMER_ID))
        if (response.isSuccessful) {
            data = response.body()!!
            campaignlist.clear()
            signcampaignlist.clear()
            donecampaignlist.clear()
            campaignlist = (response.body()?.newCampaigns as ArrayList<NewCampaignsItem>?)!!
            signcampaignlist = (response.body()?.signed as ArrayList<NewCampaignsItem>?)!!
            donecampaignlist = (response.body()?.completed as ArrayList<NewCampaignsItem>?)!!

            if (type.equals("1")) {
                if (campaignlist.size > 0) {
                    adapter = CampainListAdapter(this, campaignlist, "1")
                } else {
                    ToastUtils.showShort("No Campaigns Found")
                }
            } else if (type.equals("2")) {
                if (signcampaignlist.size > 0) {
                    adapter = CampainListAdapter(this, signcampaignlist, "2")
                } else {
                    ToastUtils.showShort("No Campaigns Found")
                }
            } else if (type.equals("3")) {
                if (donecampaignlist.size > 0) {
                    adapter = CampainListAdapter(this, donecampaignlist, "3")
                } else {
                    ToastUtils.showShort("No Campaigns Found")
                }

            }

            binding.recyclerView.adapter = adapter
            adapter?.notifyDataSetChanged()
        } else {
            ToastUtils.showShort(response.errorBody().toString())
        }
        showProgress(this, false)
    }
}