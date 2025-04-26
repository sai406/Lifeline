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
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.mstech.lifeline.R
import com.mstech.lifeline.activities.LoginActivity
import com.mstech.lifeline.activities.UserdetailsActivity
import com.mstech.lifeline.api.RetrofitApi
import com.mstech.lifeline.databinding.FragmentProfileBinding
import com.mstech.lifeline.models.SharedKey
import com.mstech.lifeline.utils.Utils
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
        lifecycleScope.launch {
            getMemberProfile()
        }
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

    private suspend fun getMemberProfile() {
        Utils.showProgress(requireActivity(), true)
        val response =
            RetrofitApi().getMemberProfile(SPStaticUtils.getString(SharedKey.CUSTOMER_ID))
        if (response.isSuccessful) {
            binding.name.setText(response.body()?.FirstName+response.body()?.LastName)
            binding.email.setText(response.body()?.EmailId.toString())
            binding.mobile.setText(response?.body()?.Mobile.toString())
            binding.address.setText(response?.body()?.GeoAddress)
            Glide.with(requireActivity())  //2
                .load(response?.body()?.CustomerImagePath) //3
                .placeholder(R.drawable.ic_loading) //5
                .error(R.drawable.profileperson) //6
                .centerInside()
                .into(binding.profilePic)
        } else {
            ToastUtils.showShort(response.errorBody()?.string())
        }
        Utils.showProgress(requireContext(), false)

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
