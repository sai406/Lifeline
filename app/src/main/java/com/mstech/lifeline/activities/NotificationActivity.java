package com.mstech.lifeline.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.blankj.utilcode.util.SPStaticUtils;
import com.mstech.lifeline.R;
import com.mstech.lifeline.adapter.ReqestSentAdapter;
import com.mstech.lifeline.adapter.RequestPendingAdapter;
import com.mstech.lifeline.models.ReqestPendingModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * HARISH GADDAM
 */

public class NotificationActivity extends BaseActivity {

    private RecyclerView rvRequestPending;
    private RecyclerView rvRequestSent;
    private TextView tvEmptyRequestPending;
    private TextView tvEmptyRequestSent;

    SharedPreferences sharedPreferences;
    ArrayList<ReqestPendingModel> listRequestPending;
    ArrayList<ReqestPendingModel> listRequestSent;
    private String strRequestsent = "";
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        setActionBarTitle();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Notifications");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        listRequestPending = new ArrayList<>();
        listRequestSent = new ArrayList<>();
        rvRequestPending = findViewById(R.id.rvRequestPending);
        tvEmptyRequestPending = findViewById(R.id.tvEmptyRequestPending);
        tvEmptyRequestSent = findViewById(R.id.tvEmptyRequestSent);
        rvRequestSent = findViewById(R.id.rvRequestSent);


        if (isNetworkConnected()) {
            getAllRequestListFromServer();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    private void setActionBarTitle() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("Notifications");
    }

    /**
     * List from server for Request pending and sent
     */

    public void getAllRequestListFromServer() {
        showPDialog("Loading ...");
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        String url = "http://civiccare.net/api/GetMemberFriends?mid=" + SPStaticUtils.getString("customerid") + "&status=2&srchname=";
//        String url = "http://gtcmaustralia.org/api/GetCustomerFriends?cid=34&status=2&srchname=";
        Log.d("url: ", url);
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("res_Notifications: ", "" + response.toString());
                        hidePDialog();
                        if (response.toString().contains("MemberId")) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject obj = response.getJSONObject(i);
                                    strRequestsent = obj.optString("RequestSent");
//                                    Log.e("strRequestsent: ", strRequestsent);

                                    if (obj.has("RequestSent")) {
                                        if (obj.getString("RequestSent").equalsIgnoreCase("0")) {
                                            ReqestPendingModel reqestPendingModel = new ReqestPendingModel(
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
                                            );
                                            listRequestPending.add(reqestPendingModel);

                                            if (listRequestPending.size() == 0) {
                                                tvEmptyRequestPending.setVisibility(View.VISIBLE);
                                                rvRequestPending.setVisibility(View.GONE);
                                            } else {
                                                tvEmptyRequestPending.setVisibility(View.GONE);
                                                rvRequestPending.setVisibility(View.VISIBLE);
                                                assert listRequestPending != null;

                                                rvRequestPending.setHasFixedSize(true);
                                                rvRequestPending.setNestedScrollingEnabled(false);
                                                rvRequestPending.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                                                RequestPendingAdapter mPendingAdapter = new RequestPendingAdapter(mContext, listRequestPending);
                                                rvRequestPending.setAdapter(mPendingAdapter);
                                            }
                                        } else {
                                            ReqestPendingModel reqestPendingModel = new ReqestPendingModel(
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
                                            );
                                            listRequestSent.add(reqestPendingModel);

                                            if (listRequestSent.size() == 0) {
                                                tvEmptyRequestSent.setVisibility(View.VISIBLE);
                                                rvRequestSent.setVisibility(View.GONE);
                                            } else {
                                                tvEmptyRequestSent.setVisibility(View.GONE);
                                                rvRequestSent.setVisibility(View.VISIBLE);
                                                assert listRequestSent != null;

                                                rvRequestSent.setHasFixedSize(true);
                                                rvRequestSent.setNestedScrollingEnabled(false);
                                                rvRequestSent.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                                                ReqestSentAdapter mSentAdapter = new ReqestSentAdapter(mContext, listRequestSent);
                                                rvRequestSent.setAdapter(mSentAdapter);
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            Log.e("Pending_Size: ", " " + listRequestPending.size());
                            Log.e("Sent_Size: ", " " + listRequestSent.size());

                            /*// if both list empty then show empty message
                            if (listRequestPending.size() == 0) {
                                rvRequestPending.setVisibility(View.GONE);
                                tvEmptyRequestPending.setVisibility(View.VISIBLE);
                            } else if (listRequestSent.size() == 0) {
                                rvRequestSent.setVisibility(View.GONE);
                                tvEmptyRequestSent.setVisibility(View.VISIBLE);
                            }*/
                        } /*else {
                            dialog = new Dialog(mContext);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.alert1);
                            dialog.setCancelable(false);
                            TextView alertmessage=(TextView)dialog.findViewById(R.id.resultalerttvid);
                            alertmessage.setText("No Notifications.");
                            Button okbtn = (Button) dialog.findViewById(R.id.okbtnid);
                            okbtn.setText("Ok ");
                            Button cancel = (Button) dialog.findViewById(R.id.cancelbtn);
                            cancel.setVisibility(View.GONE);
                            okbtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onCl7ick(View v) {
                                    dialog.dismiss();
                                    mContext.startActivity(new Intent(mContext, Home.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                }
                            });
                            dialog.show();
                        }*/
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("", "Error: " + error.getMessage());
                hidePDialog();
            }
        });
        requestQueue.add(movieReq);
    }
}