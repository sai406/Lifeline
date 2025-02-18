package com.mstech.lifeline.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.blankj.utilcode.util.SPStaticUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mstech.lifeline.R;
import com.mstech.lifeline.activities.ChatListFriendsActivity;
import com.mstech.lifeline.activities.Newchatmessage;
import com.mstech.lifeline.adapter.ChatAdapter;
import com.mstech.lifeline.models.ChatModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/** HARISH GADDAM  */

public class Chat extends Fragment {

    private Context mContext;
    private ChatAdapter adapter;
    RecyclerView Eventsrec;
    private TextView tvInfo;
    private Button btnSendRquest;
    SharedPreferences sharedPreferences;

    String custid,merch;
    FloatingActionButton newchat;
    LinearLayoutManager mLayoutManager;
    private List<ChatModel> Eventlist = new ArrayList<ChatModel>();

    public Chat() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_chat, container, false);
        mContext = v.getContext();
        Eventsrec = (RecyclerView) v.findViewById(R.id.gridViewchat);

        tvInfo = v.findViewById(R.id.tvInfo);
        btnSendRquest = v.findViewById(R.id.btnSendRquest);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        custid= SPStaticUtils.getString("customerid");
        newchat=v.findViewById(R.id.fab);

        newchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Newchatmessage.class).putExtra("from","private");
                startActivity(intent);
            }
        });


//        final Handler handler = new Handler();
//        final int delay = 60000; //milliseconds
//
//        handler.postDelayed(new Runnable(){
//            public void run(){
//                GETEVENTS();
//                handler.postDelayed(this, delay);
//            }
//        }, delay);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        GETEVENTS();
    }

    public void GETEVENTS() {
        Eventlist.clear();

        RequestQueue requestQueue= Volley.newRequestQueue(mContext);

//        String url = "http://151.106.38.222:1000/api/GetChatList?cid=11";
        String url = "http://civiccare.net/api/GetChatList?mid=" + custid;
        Log.d("sss","url:"+url);
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("message", response.toString());

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                ChatModel Members=new ChatModel();
                                Members.setChatID(obj.getLong("ChatId"));
                                Members.setCustomerID(obj.getLong("MemberId"));
                                Members.setLastMessageID(obj.getLong("LastMessageId"));
                                Members.setMessageText(obj.getString("MessageText"));
                                Members.setName(obj.getString("Name"));
                                Members.setImagePath(obj.getString("ImagePath"));
                                Members.setVideoPath(obj.getString("VideoPath"));
                                Members.setProfileImage(obj.getString("ProfileImage"));
                                Members.setLastMessageDatestring(obj.getString("LastMessageDatestring"));
                                Members.setCreatedDateString(obj.getString("CreatedDateString"));

                                Eventlist.add(Members);
                                Log.e("Eventlist: ", "" + Eventlist.size());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // hide show
                        if (Eventlist.size() > 0) {
                            tvInfo.setVisibility(View.GONE);
                            btnSendRquest.setVisibility(View.GONE);
                            Eventsrec.setVisibility(View.VISIBLE);
                            adapter=new ChatAdapter(getContext(), Eventlist);
                            mLayoutManager=new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            Eventsrec.setLayoutManager(mLayoutManager);
                            Eventsrec.setAdapter(adapter);
//                            newchat.setVisibility(View.VISIBLE);

                        } else {
                            tvInfo.setVisibility(View.VISIBLE);
                            btnSendRquest.setVisibility(View.VISIBLE);
                            Eventsrec.setVisibility(View.GONE);
//                            newchat.setVisibility(View.GONE);

                            btnSendRquest.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getContext(), ChatListFriendsActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }

//                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(movieReq);
    }

}
