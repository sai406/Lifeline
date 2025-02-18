package com.mstech.lifeline.resources.activities

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.mstech.lifeline.activities.BaseActivity
import com.mstech.lifeline.api.RetrofitApi
import com.mstech.lifeline.databinding.ActivitySosListBinding
import com.mstech.lifeline.resources.adapter.ResourceListAdapter
import kotlinx.coroutines.launch

class ResourcetActivity : BaseActivity() {
    lateinit var binding: ActivitySosListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySosListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.includeTop.ivBack.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        binding.includeTop.tvHeader.setText("Resources")
        binding.recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        lifecycleScope.launch {
            getResource()
        }
    }

    private suspend fun getResource() {
        showPDialog("Please wait ..")
        val response = RetrofitApi().getResources()
        if (response.isSuccessful) {
            binding.recyclerView.adapter = response.body()?.let {
                ResourceListAdapter(
                    this,
                    it, this
                )
            }
        } else {
            ToastUtils.showShort(response.errorBody()?.string())
        }
        hidePDialog()
    }


}