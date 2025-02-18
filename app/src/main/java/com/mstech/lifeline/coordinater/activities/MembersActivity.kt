package com.mstech.lifeline.coordinater.activities

import android.os.Bundle
import android.os.StrictMode
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.SPStaticUtils
import com.blankj.utilcode.util.ToastUtils
import com.mstech.lifeline.models.SharedKey
import com.mstech.lifeline.api.RetrofitApi
import com.mstech.lifeline.coordinater.adapters.MembersAdapter
import com.mstech.lifeline.databinding.ActivityMembersBinding
import com.mstech.lifeline.utils.Utils
import kotlinx.coroutines.launch
import layout.CoordinatorMemberResponse

class MembersActivity : AppCompatActivity() {
    var adapte: MembersAdapter? = null
    lateinit var binding: ActivityMembersBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMembersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerView.layoutManager = GridLayoutManager(
            this,
            2
        )
        supportActionBar?.hide()
        binding.includeHeader.ivBack.setOnClickListener(View.OnClickListener { view: View? -> onBackPressed() })

       binding.includeHeader.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (adapte!=null){
                    adapte?.filter?.filter(editable.toString())
                }

            }
        })

        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        lifecycleScope.launch {
            getMembers()
        }

    }
    private suspend fun getMembers() {
        Utils.showProgress(this,true)
        var response = RetrofitApi().getMembers(SPStaticUtils.getString(SharedKey.CUSTOMER_ID))
        if (response.isSuccessful) {
            var data = response.body()
            adapte =
                MembersAdapter(this, data as List<CoordinatorMemberResponse>)
            binding.recyclerView.adapter = adapte
        } else {
            ToastUtils.showShort(response.errorBody()?.string())
        }
        Utils.showProgress(this,false)
    }



}