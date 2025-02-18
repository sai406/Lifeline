package com.mstech.lifeline.organisation.activities

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.mstech.lifeline.R
import com.mstech.lifeline.activities.BaseActivity
import com.mstech.lifeline.databinding.ActivityOrganisationListBinding
import com.mstech.lifeline.organisation.adapter.OrganisationListAdapter
import com.mstech.lifeline.organisation.model.RetroApi

import kotlinx.coroutines.launch
import java.util.*

class OrganisationListActivity : BaseActivity() {

    lateinit var binding: ActivityOrganisationListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrganisationListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
        )
        lifecycleScope.launch {
            getOrganisationlist()
        }
    }

    private suspend fun getOrganisationlist() {
        showPDialog("Pleasewait ..")
        val response = RetroApi().getOrganisationList()
        if (response.isSuccessful) {
            if(response.body()?.size==0){
                val dialog = Dialog(mContext)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(false)
                dialog.setContentView(R.layout.layout_alert_dialog)
                Objects.requireNonNull(dialog.window)?.setBackgroundDrawable(
                    ColorDrawable(
                        Color.TRANSPARENT
                    )
                )

                val tvQuantity = dialog.findViewById<TextView>(R.id.tvQuantity)
                tvQuantity.text = "No Organisations found at the moment."
                dialog.findViewById<View>(R.id.tvOK).setOnClickListener { view: View? ->
                    dialog.dismiss()
                    onBackPressed()
                }
                dialog.show()
            }else {
                binding.recyclerView.adapter = response.body()?.let {
                    OrganisationListAdapter(
                        this,
                        it
                    )
                }
            }
        } else {
            ToastUtils.showShort(response.errorBody()?.string())
        }
        hidePDialog()
    }
}
