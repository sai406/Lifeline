package com.mstech.lifeline.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.SPStaticUtils
import com.bumptech.glide.Glide
import com.mstech.lifeline.models.SharedKey
import com.mstech.lifeline.R
import com.mstech.lifeline.activities.LoginActivity
import com.mstech.lifeline.activities.UserdetailsActivity
import com.mstech.lifeline.databinding.FragmentProfileBinding
import com.mstech.lifeline.vault.activities.PinActivity
import kotlinx.coroutines.launch


class ProfileFragment : Fragment(R.layout.fragment_home) {
    lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.name.setText(SPStaticUtils.getString(SharedKey.NAME))
        binding.email.setText(SPStaticUtils.getString(SharedKey.EMAIL))
        binding.mobile.setText(SPStaticUtils.getString(SharedKey.MOBILE))
        binding.address.setText(SPStaticUtils.getString(SharedKey.ADDRESS))
        Glide.with(requireActivity())  //2
            .load(SPStaticUtils.getString(SharedKey.PROFILEPIC,"")) //3
            .placeholder(R.drawable.ic_loading) //5
            .error(R.drawable.profileperson) //6
            .centerInside()
            .into(binding.profilePic)
        binding.profile.setOnClickListener(View.OnClickListener {
            startActivity(Intent(requireActivity(), UserdetailsActivity::class.java))
        })
        binding.vault.setOnClickListener(View.OnClickListener {
            startActivity(Intent(requireActivity(), PinActivity::class.java))
        })
        binding.logout.setOnClickListener(View.OnClickListener {
            showDialog("Are you sure want to logout?", "", requireActivity())
        })
        return binding.root
    }

    fun showDialog(title: String?, Message: String?, context: Context) {
        val builder = android.app.AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setIcon(context.resources.getDrawable(R.mipmap.ic_launcher))
        builder.setCancelable(true)
        builder.setMessage(Message)
        val positiveText = context.getString(android.R.string.ok)
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, which -> //opration do here on Click "Close"
            dialog.dismiss()
        }
        builder.setPositiveButton(
            "OK"
        ) { dialog, which -> //opration do here on Click "Close"
            dialog.dismiss()
            lifecycleScope.launch {

                SPStaticUtils.clear()
                startActivity(
                    Intent(
                        requireActivity(),
                        LoginActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                )
            }
        }
        val dialog = builder.create()
        // display dialog
        dialog.show()
    }

}
