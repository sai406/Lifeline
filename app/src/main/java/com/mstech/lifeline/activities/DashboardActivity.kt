package com.mstech.lifeline.activities

import com.mstech.lifeline.utils.SpeechToTextService
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.blankj.utilcode.util.SPStaticUtils
import com.mstech.lifeline.models.SharedKey
import com.mstech.lifeline.R
import com.mstech.lifeline.api.RetrofitApi
import com.mstech.lifeline.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {
lateinit var binding : ActivityDashboardBinding
lateinit var navView: BottomNavigationView
lateinit var navController: NavController
var i=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navView = findViewById(R.id.nav_view)

        navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard,R.id.navigation_campaign, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
//        navView.setBadge(R.id.navigation_campaign,i)
        supportActionBar?.hide()
        if (!isServiceRunning(SpeechToTextService::class.java)) {
            // Start the foreground service
            val serviceIntent = Intent(this, SpeechToTextService::class.java)
            startService(serviceIntent)
        } else {
            // The service is already running
            // You can take appropriate action here
        }
        if (!isServiceRunning(VoiceService::class.java)) {
            // Start the foreground service
            val serviceIntent = Intent(this, VoiceService::class.java)
            startService(serviceIntent)
        } else {
            // The service is already running
            // You can take appropriate action here
        }

    }
    fun BottomNavigationView.setBadge(tabResId: Int, badgeValue: Int) {
        getOrCreateBadge(this, tabResId)?.let { badge ->
            badge.visibility = if (badgeValue >= 0) {
                badge.text = "$badgeValue"
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    @SuppressLint("ServiceCast")
    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val services = manager.getRunningServices(Int.MAX_VALUE)

        for (service in services) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }

        return false
    }

    private fun getOrCreateBadge(bottomBar: View, tabResId: Int): TextView? {
        val parentView = bottomBar.findViewById<ViewGroup>(tabResId)
        return parentView?.let {
            var badge = parentView.findViewById<TextView>(R.id.menuItemBadge)
            if (badge == null) {
                LayoutInflater.from(parentView.context).inflate(R.layout.bottom_nav_badge, parentView, true)
                badge = parentView.findViewById(R.id.menuItemBadge)
            }
            badge
        }
    }
    override fun onResume() {
        super.onResume()
//        lifecycleScope.launch {
//            getCampains()
//        }
    }
    private suspend  fun getCampains(){
        val response = RetrofitApi().getCampaignList(SPStaticUtils.getString(SharedKey.CUSTOMER_ID))
        if (response.isSuccessful) {
          i = response.body()?.newCampaigns!!.size
//            navView.setupWithNavController(navController)
            navView.setBadge(R.id.navigation_campaign,i)
        }
    }
}