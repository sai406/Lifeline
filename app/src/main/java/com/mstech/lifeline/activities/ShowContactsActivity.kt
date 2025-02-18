package com.mstech.lifeline.activities

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
import com.mstech.lifeline.adapter.SOSListAdapter
import com.mstech.lifeline.api.RetrofitApi
import com.mstech.lifeline.databinding.ActivityShowContactsBinding
import com.mstech.lifeline.models.SOScontactsItem
import com.mstech.lifeline.utils.Utils
import kotlinx.coroutines.launch

class ShowContactsActivity : AppCompatActivity() {
    lateinit var binding: ActivityShowContactsBinding
    lateinit var adapter: SOSListAdapter
    var size: Int = 0
    var list: ArrayList<SOScontactsItem> = ArrayList()

    companion object {
        val RECEIVER_INTENT = "RECEIVER_INTENT"
        val RECEIVER_MESSAGE = "RECEIVER_MESSAGE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.includeTop.ivBack.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        binding.includeTop.tvHeader.setText("SOS Contacts")
        binding.recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        adapter =
            SOSListAdapter(this, list)

        binding.add.setOnClickListener(View.OnClickListener {
            if (size < 3) {
                startActivity(Intent(this, AddSosActivity::class.java))
            } else {
                ToastUtils.showShort("Only 3 SOS Contacts can add.")
            }

        })
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            mMessageReceiver,
            IntentFilter(RECEIVER_INTENT)
        )
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            if (NetworkUtils.isConnected()) {
                lifecycleScope.launch {
                    getCoordinators()
                }
            } else {
                ToastUtils.showShort("No Internet Connection")
            }
        }
    }

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            // Extract data included in the Intent
            val message = intent.getStringExtra("RECEIVER_MESSAGE")
            Log.d("receiver", "Got message: $message")


            if (NetworkUtils.isConnected()) {
                lifecycleScope.launch {
                    if (message != null) {
                        deleteSOS(message)
                    }
                }
            } else {
                ToastUtils.showShort("No Internet Connection")
            }

        }
    }

    override fun onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver)
        super.onStop()
    }

    private suspend fun getCoordinators() {
        Utils.showProgress(this, true)
        if (list != null) {
            list.clear()
        }
        val response = RetrofitApi().getCoordinators(SPStaticUtils.getString(SharedKey.CUSTOMER_ID))
        Utils.showProgress(this, false)
        if (response.isSuccessful) {
            if (response.body()?.sOScontacts?.size!! > 0) {
                size = response.body()?.sOScontacts?.size!!
                list = response.body()?.sOScontacts as ArrayList<SOScontactsItem>
                adapter =
                    SOSListAdapter(this, list)
                binding.recyclerView.adapter = adapter
                binding.textMsg.visibility = View.GONE
                adapter.notifyDataSetChanged()
            } else {
                binding.textMsg.visibility = View.VISIBLE
                binding.textMsg.setText("You have not saved any SOS contacts")
            }
            adapter.notifyDataSetChanged()
        } else {
            ToastUtils.showShort("Error")
        }
    }

    private suspend fun deleteSOS(message: String) {
        Utils.showProgress(this, true)
        val response = RetrofitApi().deleteSos(message)
        Utils.showProgress(this, false)
        if (response.isSuccessful) {
            var data = response.body()
            if (data?.statusCode!! >= 0) {
                ToastUtils.showShort(data?.statusMessage)
                getCoordinators()
            } else {
                ToastUtils.showShort(data?.statusMessage)
            }
        } else {
            ToastUtils.showShort("Error")
        }
    }
}