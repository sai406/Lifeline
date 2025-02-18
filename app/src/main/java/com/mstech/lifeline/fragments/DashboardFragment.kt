package com.mstech.lifeline.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.SPStaticUtils
import com.mstech.lifeline.models.SharedKey
import com.mstech.lifeline.R
import com.mstech.lifeline.activities.CampaignListActivity
import com.mstech.lifeline.activities.GalleryListActivity
import com.mstech.lifeline.activities.ShowContactsActivity
import com.mstech.lifeline.coordinater.activities.IncidentListActivity
import com.mstech.lifeline.coordinater.activities.MembersActivity
import com.mstech.lifeline.databinding.FragmentDashboardBinding
import com.mstech.lifeline.resources.activities.ResourcetActivity


class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    lateinit var binding: FragmentDashboardBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentDashboardBinding = FragmentDashboardBinding.inflate(inflater, container, false)
        binding = fragmentDashboardBinding
        if (SPStaticUtils.getString(SharedKey.ISVOLUNTEER).equals("1")) {
            binding.sos.visibility = View.VISIBLE
        } else {
            binding.sos.visibility = View.GONE
        }
        if (SPStaticUtils.getString(SharedKey.ISVOLUNTEER).equals("2")) {
            binding.cordinaterlist.visibility = View.VISIBLE
            binding.memberList.visibility = View.VISIBLE
        } else {
            binding.cordinaterlist.visibility = View.GONE
            binding.memberList.visibility = View.GONE
        }
        binding.addsos.setOnClickListener(View.OnClickListener {
            startActivity(Intent(requireActivity(), ShowContactsActivity::class.java))
        })
        binding.memberList.setOnClickListener(View.OnClickListener {
            startActivity(Intent(requireActivity(), MembersActivity::class.java))
        })
        binding.cordinaterlist.setOnClickListener(View.OnClickListener {
            startActivity(Intent(requireActivity(), IncidentListActivity::class.java))
        })

        binding.campaign.setOnClickListener(View.OnClickListener {
            startActivity(Intent(requireActivity(), CampaignListActivity::class.java))
        })
        binding.sos.setOnClickListener(View.OnClickListener {
            startActivity(Intent(requireActivity(), IncidentListActivity::class.java))
        })
        binding.gallery.setOnClickListener(View.OnClickListener {
            startActivity(Intent(requireActivity(), GalleryListActivity::class.java))
        })

        binding.resources.setOnClickListener(View.OnClickListener {
            startActivity(Intent(requireActivity(), ResourcetActivity::class.java))
        })

        return binding.root
    }
}

