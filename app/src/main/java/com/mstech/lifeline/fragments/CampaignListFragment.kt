package com.mstech.lifeline.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

class CampaignListFragment : Fragment() {
    lateinit var binding : ActivityCampaignListBinding
    var campaignlist = mutableListOf<NewCampaignsItem>()
    var adapter: CampainListAdapter? = null
    var data: CampaignResponse? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityCampaignListBinding.inflate(layoutInflater,container,false)
        val linearLayoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerView?.layoutManager = linearLayoutManager

        binding.open.setOnClickListener(View.OnClickListener {
            binding.open.setBackgroundColor(resources.getColor(R.color.purple_700))
            binding.signed.setBackgroundColor(resources.getColor(R.color.quantum_grey))
            binding.done.setBackgroundColor(resources.getColor(R.color.quantum_grey))
            campaignlist = data?.newCampaigns!!
            adapter = campaignlist.let {
                it?.let { it1 ->
                    CampainListAdapter(
                        requireActivity(),
                        it1,
                        "1"
                    )
                }
            }
            binding.recyclerView.adapter = adapter
            adapter?.notifyDataSetChanged()
        })
        binding.signed.setOnClickListener(View.OnClickListener {
            binding.signed.setBackgroundColor(resources.getColor(R.color.purple_700))
            binding.open.setBackgroundColor(resources.getColor(R.color.quantum_grey))
            binding.done.setBackgroundColor(resources.getColor(R.color.quantum_grey))
            campaignlist = data?.signed!!
            adapter = campaignlist.let {
                it?.let { it1 ->
                    CampainListAdapter(
                        requireActivity(),
                        it1,
                        "2"
                    )
                }
            }
            binding.recyclerView.adapter = adapter
            adapter?.notifyDataSetChanged()
        })
        binding.done.setOnClickListener(View.OnClickListener {
            binding.done.setBackgroundColor(resources.getColor(R.color.purple_700))
            binding.signed.setBackgroundColor(resources.getColor(R.color.quantum_grey))
            binding.open.setBackgroundColor(resources.getColor(R.color.quantum_grey))
            campaignlist = data?.completed!!
            adapter = campaignlist.let {
                it?.let { it1 ->
                    CampainListAdapter(
                        requireActivity(),
                        it1,
                        "3"
                    )
                }
            }
            binding.recyclerView.adapter = adapter
            adapter?.notifyDataSetChanged()
        })
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            getCampains()
        }
    }
    private suspend fun getCampains() {
        campaignlist.clear()
        showProgress(requireActivity(), true)
        val response = RetrofitApi().getCampaignList(SPStaticUtils.getString(SharedKey.CUSTOMER_ID))
        if (response.isSuccessful) {
               data =response.body()
                campaignlist = response.body()?.newCampaigns!!
                adapter = campaignlist.let {
                    it?.let { it1 ->
                        CampainListAdapter(
                            requireActivity(),
                            it1,
                            "1"
                        )
                    }
                }
            binding.open.setBackgroundColor(resources.getColor(R.color.purple_700))
            binding.signed.setBackgroundColor(resources.getColor(R.color.quantum_grey))
            binding.done.setBackgroundColor(resources.getColor(R.color.quantum_grey))
            binding.recyclerView.adapter = adapter
                adapter?.notifyDataSetChanged()
            }else {
            ToastUtils.showShort(response.errorBody().toString())
        }
        showProgress(requireActivity(), false)
    }
}