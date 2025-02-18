package com.mstech.lifelinecoordinator.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.SPStaticUtils
import com.blankj.utilcode.util.ToastUtils
import com.mstech.lifeline.models.SharedKey
import com.mstech.lifeline.api.RetrofitApi
import com.mstech.lifeline.coordinater.adapters.VolunteersAdapter
import com.mstech.lifeline.coordinater.model.VolunteerListResponse
import com.mstech.lifeline.databinding.ActivityVolunteersListBinding
import com.mstech.lifeline.utils.Utils

import kotlinx.coroutines.launch

class VolunteersListActivity : AppCompatActivity() {
    lateinit var binding: ActivityVolunteersListBinding
    var ids: String = ""
    var id: String = ""

    companion object {
        val RECEIVER_INTENT = "RECEIVER_INTENT"
        val RECEIVER_MESSAGE = "RECEIVER_MESSAGE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Volunteers"
        id = intent.extras?.getString("id").toString()
        binding = ActivityVolunteersListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )

        binding.done.setOnClickListener(View.OnClickListener {
            lifecycleScope.launch {
                if (!NetworkUtils.isConnected()) {
                    ToastUtils.showShort("No Internet ")
                } else if (ids.trim().equals("")) {
                    ToastUtils.showShort("Select atleast one Volunteer ")
                } else {
                    assignVolunteers()
                }

            }
        })
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            getVolunteers()
        }
    }
    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            mMessageReceiver,
            IntentFilter(RECEIVER_INTENT)
        )
    }

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            // Extract data included in the Intent
            val message = intent.getStringExtra("RECEIVER_MESSAGE")
            ids = message!!.replace("[", "").replace("]", "")
            Log.d("receiver", "Got message: $message")
        }
    }

    override fun onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver)
        super.onStop()
    }

    private suspend fun getVolunteers() {
        Utils.showProgress(this,true)
        var response = RetrofitApi().getVolunteers(SPStaticUtils.getInt(SharedKey.CUSTOMER_ID))
        if (response.isSuccessful) {
            var data = response.body()
            binding.recyclerView.adapter =
                VolunteersAdapter(this, data as List<VolunteerListResponse>)
        } else {
            ToastUtils.showShort(response.errorBody()?.string())
        }
        Utils.showProgress(this,false)
    }

    private suspend fun assignVolunteers() {
        Utils.showProgress(this,true)
        var response = RetrofitApi().assignVolunteer(id, ids)
        if (response.isSuccessful) {
            var data = response.body()

            if (data?.statusCode.toString().equals("0")){
                ToastUtils.showShort(data?.statusMessage)
                onBackPressed()
            }else{
                ToastUtils.showShort(data?.statusMessage)
            }
        } else {
            ToastUtils.showShort(response.errorBody()?.string())
        }
        Utils.showProgress(this,false)
    }
}