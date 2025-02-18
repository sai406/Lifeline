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
import com.mstech.lifeline.activities.Newchatmessage;
import com.mstech.lifeline.activities.PublicPostActivity;
import com.mstech.lifeline.adapter.WallmessageAdapter;
import com.mstech.lifeline.models.Wallmessages;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Message extends Fragment {

    private WallmessageAdapter adapter;
    RecyclerView Eventsrec;
    SharedPreferences sharedPreferences;
    Context mContext;
    FloatingActionButton fab;
    String custid,merch;
    LinearLayoutManager mLayoutManager;
    private List<Wallmessages> Eventlist = new ArrayList<Wallmessages>();
    public Message() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_message, container, false);
        mContext = v.getContext();
        Eventsrec = (RecyclerView) v.findViewById(R.id.gridViewmessage);
        fab = (FloatingActionButton) v.findViewById(R.id.fab);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        custid= SPStaticUtils.getString("customerid");

        adapter=new WallmessageAdapter(getContext(), Eventlist);
        mLayoutManager=new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        Eventsrec.setLayoutManager(mLayoutManager);
        Eventsrec.setAdapter(adapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireActivity(), Newchatmessage.class).putExtra("from","public"));
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        GETEVENTS();
    }

    public void GETEVENTS() {
 Eventlist.clear();
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity()); //http://gtcmaustralia.org/api/
//        String url = "http://151.106.38.222:1000/api/getcustomerwallmessages?orgid=1&cid=11&pgsize=-1&pgindex=1&str=&sortby=1";
        String url = "http://civiccare.net/api/GetMemberWallPostMessages?mid=" + SPStaticUtils.getString("customerid") + "&pgsize=-1&pgindex=1&str=&sortby=1";
        Log.d("sss","url:"+url);
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("chat", response.toString());


                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                Wallmessages Members=new Wallmessages();
                                Members.setGmsgID(obj.getLong("GMSGId"));
                                Members.setCustomerID(obj.getLong("MemberId"));
                                Members.setMessage(obj.getString("Message"));
                                Members.setCustomerImagePath(obj.getString("CustomerImagePath"));
                                Members.setFormatedPostedDate(obj.getString("FormatedPostedDate"));
                                Members.setStatus(obj.getLong("Status"));
                                Members.setSenderName(obj.getString("SenderName"));
                                Members.setSender(obj.getLong("Sender"));
                                Members.setImagepath(obj.getString("Imagepath"));
                                Members.setVideopath(obj.getString("Videopath"));
                                Eventlist.add(Members);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(movieReq);
    }
}
