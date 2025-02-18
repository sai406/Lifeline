package com.mstech.lifeline.fragments

import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.VolleyLog
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.blankj.utilcode.util.SPStaticUtils
import com.mstech.lifeline.R
import com.mstech.lifeline.activities.*
import com.mstech.lifeline.adapter.ReqestSentAdapter
import com.mstech.lifeline.adapter.RequestPendingAdapter
import com.mstech.lifeline.databinding.FragmentNotificationsBinding
import com.mstech.lifeline.models.ReqestPendingModel
import org.json.JSONException
import java.util.*

class NotificationsFragment : Fragment(R.layout.fragment_notifications) {
    lateinit var binding: FragmentNotificationsBinding
    private var rvRequestPending: RecyclerView? = null
    private var rvRequestSent: RecyclerView? = null
    private var tvEmptyRequestPending: TextView? = null
    private var tvEmptyRequestSent: TextView? = null

    var sharedPreferences: SharedPreferences? = null
    var listRequestPending: ArrayList<ReqestPendingModel>? = null
    var listRequestSent: ArrayList<ReqestPendingModel>? = null
    private var strRequestsent = ""
    var dialog: Dialog? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentNotificationsBinding =
            FragmentNotificationsBinding.inflate(inflater, container, false)
        binding = fragmentNotificationsBinding
        listRequestPending = ArrayList()
        listRequestSent = ArrayList()
        rvRequestPending = binding.root.findViewById<RecyclerView>(R.id.rvRequestPending)
        tvEmptyRequestPending = binding.root.findViewById<TextView>(R.id.tvEmptyRequestPending)
        tvEmptyRequestSent = binding.root.findViewById<TextView>(R.id.tvEmptyRequestSent)
        rvRequestSent = binding.root.findViewById<RecyclerView>(R.id.rvRequestSent)

            getAllRequestListFromServer()

        return binding.root
    }
    fun getAllRequestListFromServer() {
        val requestQueue = Volley.newRequestQueue(requireContext())
        val url =
            "http://civiccare.net/api/GetMemberFriends?mid=" + SPStaticUtils.getString("customerid") + "&status=2&srchname="
        //        String url = "http://gtcmaustralia.org/api/GetCustomerFriends?cid=34&status=2&srchname=";
        Log.d("url: ", url)
        val movieReq = JsonArrayRequest(url,
            { response ->
                Log.e("res_Notifications: ", "" + response.toString())
                if (response.toString().contains("MemberId")) {
                    for (i in 0 until response.length()) {
                        try {
                            val obj = response.getJSONObject(i)
                            strRequestsent = obj.optString("RequestSent")
                            //                                    Log.e("strRequestsent: ", strRequestsent);
                            if (obj.has("RequestSent")) {
                                if (obj.getString("RequestSent").equals("0", ignoreCase = true)) {
                                    val reqestPendingModel = ReqestPendingModel(
                                        obj.getString("MemberId"),
                                        obj.getString("FirstName"),
                                        obj.getString("LastName"),
                                        obj.getString("Mobile"),
                                        obj.getString("EmailId"),
                                        obj.getString("CustomerImagePath"),
                                        obj.getString("IsFriend"),
                                        obj.getString("RequestStatus"),
                                        obj.getString("RequestSent"),
                                        obj.getString("RequestStatus")
                                    )
                                    listRequestPending!!.add(reqestPendingModel)
                                    if (listRequestPending!!.size == 0) {
                                        tvEmptyRequestPending!!.visibility = View.VISIBLE
                                        rvRequestPending!!.visibility = View.GONE
                                    } else {
                                        tvEmptyRequestPending!!.visibility = View.GONE
                                        rvRequestPending!!.visibility = View.VISIBLE
                                        assert(listRequestPending != null)
                                        rvRequestPending!!.setHasFixedSize(true)
                                        rvRequestPending!!.isNestedScrollingEnabled = false
                                        rvRequestPending!!.layoutManager = LinearLayoutManager(
                                            requireContext(),
                                            LinearLayoutManager.VERTICAL,
                                            false
                                        )
                                        val mPendingAdapter =
                                            RequestPendingAdapter(requireContext(), listRequestPending)
                                        rvRequestPending!!.adapter = mPendingAdapter
                                    }
                                } else {
                                    val reqestPendingModel = ReqestPendingModel(
                                        obj.getString("MemberId"),
                                        obj.getString("FirstName"),
                                        obj.getString("LastName"),
                                        obj.getString("Mobile"),
                                        obj.getString("EmailId"),
                                        obj.getString("CustomerImagePath"),
                                        obj.getString("IsFriend"),
                                        obj.getString("RequestStatus"),
                                        obj.getString("RequestSent"),
                                        obj.getString("RequestStatus")
                                    )
                                    listRequestSent!!.add(reqestPendingModel)
                                    if (listRequestSent!!.size == 0) {
                                        tvEmptyRequestSent!!.visibility = View.VISIBLE
                                        rvRequestSent!!.visibility = View.GONE
                                    } else {
                                        tvEmptyRequestSent!!.visibility = View.GONE
                                        rvRequestSent!!.visibility = View.VISIBLE
                                        assert(listRequestSent != null)
                                        rvRequestSent!!.setHasFixedSize(true)
                                        rvRequestSent!!.isNestedScrollingEnabled = false
                                        rvRequestSent!!.layoutManager = LinearLayoutManager(
                                            requireContext(),
                                            LinearLayoutManager.VERTICAL,
                                            false
                                        )
                                        val mSentAdapter =
                                            ReqestSentAdapter(requireContext(), listRequestSent)
                                        rvRequestSent!!.adapter = mSentAdapter
                                    }
                                }
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                    Log.e("Pending_Size: ", " " + listRequestPending!!.size)
                    Log.e("Sent_Size: ", " " + listRequestSent!!.size)

                    /*// if both list empty then show empty message
                                  if (listRequestPending.size() == 0) {
                                      rvRequestPending.setVisibility(View.GONE);
                                      tvEmptyRequestPending.setVisibility(View.VISIBLE);
                                  } else if (listRequestSent.size() == 0) {
                                      rvRequestSent.setVisibility(View.GONE);
                                      tvEmptyRequestSent.setVisibility(View.VISIBLE);
                                  }*/
                }
            }) { error ->
            VolleyLog.d("", "Error: " + error.message)
        }
        requestQueue.add(movieReq)
    }
}