package com.mstech.lifeline.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.blankj.utilcode.util.SPStaticUtils;
import com.mstech.lifeline.R;
import com.mstech.lifeline.adapter.ChatListFriendsAdapter;
import com.mstech.lifeline.models.ChatListModel;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/** HARISH GADDAM   */

public class ChatListFriendsActivity extends BaseActivity {

    private RecyclerView rvChatListFriends;
    private ChatListFriendsActivity mContext = ChatListFriendsActivity.this;
    SharedPreferences sharedPreferences;
//    private List<GetAllFriends> listGetFriends = new ArrayList<GetAllFriends>();
    private List<ChatListModel> listGetFriends = new ArrayList<ChatListModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list_friends);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Friends Request");

        rvChatListFriends = findViewById(R.id.rvChatListFriends);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (isNetworkConnected()) {
            getAllFriendsList();
        }
    }

    private void getAllFriendsList() {
        listGetFriends.clear();
        showPDialog("Loading ...");
        RequestQueue requestQueue= Volley.newRequestQueue(mContext);
        String url = "http://civiccare.net/api/GetAllMembers?mid="+SPStaticUtils.getString("customerid")+"&search=";
        Log.d("fr", url);
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("SendMoreFriendReq", ": " + response.toString());
                        hidePDialog();
                        if(response.toString().contains("MemberId")) {
//                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    if (!response.isNull(0)) {
                                        for (int j = 0; j < response.length(); j++) {
                                            JSONObject jsonObject = response.getJSONObject(j);
                                            ChatListModel model = new ChatListModel();
                                            model.setCustomerId(response.getJSONObject(j).getString("MemberId"));
                                            model.setFname(response.getJSONObject(j).getString("FirstName"));
                                            model.setLname(response.getJSONObject(j).getString("LastName"));
                                            model.setCustomerImagePath(response.getJSONObject(j).getString("CustomerImagePath"));
                                            model.setProfession(response.getJSONObject(j).getString("CommunityBelong"));
                                            model.setMobile(response.getJSONObject(j).getString("Mobile"));
                                            model.setEmailId(response.getJSONObject(j).getString("EmailId"));
                                            listGetFriends.add(model);
                                        }
                                    } else {
                                        Toast.makeText(mContext, "Null", Toast.LENGTH_SHORT).show();
                                    }

                                    /*GetAllFriends friends = new GetAllFriends(
                                            obj.optString("fname", "Not Found"),
                                            obj.optString("lname", "Not Found"),
                                            obj.optString("CustomerImagePath", "Not Found"),
                                            obj.optString("Mobile", "Not Found"),
                                            obj.optString("EmailId", "Not Found"),
                                            obj.optString("Profession", "Not Found"),
                                            obj.getString("CustomerId")
                                            );*/

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
//                            }

                            Log.e("size: ", " " + listGetFriends.size());
                            ChatListFriendsAdapter mAdapter = new ChatListFriendsAdapter(mContext, listGetFriends);
                            GridLayoutManager manager = new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false);
                            rvChatListFriends.setLayoutManager(manager);
                            rvChatListFriends.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }
                        else {
                           /* dialog = new Dialog(getActivity());
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.alert1);
                            dialog.setCancelable(false);
                            TextView alertmessage=(TextView)dialog.findViewById(R.id.resultalerttvid);
                            alertmessage.setText("No Messges.");
                            Button okbtn = (Button) dialog.findViewById(R.id.okbtnid);
                            okbtn.setText("Ok ");
                            Button cancel = (Button) dialog.findViewById(R.id.cancelbtn);
                            cancel.setVisibility(View.GONE);
                            okbtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();

                                }
                            });
                            dialog.show();*/
                        }
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