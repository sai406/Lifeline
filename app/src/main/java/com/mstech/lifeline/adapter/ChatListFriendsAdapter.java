package com.mstech.lifeline.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.blankj.utilcode.util.SPStaticUtils;
import com.mstech.lifeline.R;
import com.mstech.lifeline.models.ChatListModel;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * HARISH GADDAM
 */

public class ChatListFriendsAdapter extends RecyclerView.Adapter<ChatListFriendsAdapter.ViewHolerRoster> {

    Context mContext;
//    private List<GetAllFriends> listGetFriends = new ArrayList<GetAllFriends>();
    private List<ChatListModel> listGetFriends = new ArrayList<ChatListModel>();
    String date = "";
    SharedPreferences sharedPreferences;
    private ProgressDialog pDialog;
    int result = 0;
    private String strAddFriendRequest = "";

    public ChatListFriendsAdapter(Context mContext) {
        this.mContext = mContext;
    }

/*    public ChatListFriendsAdapter(Context mContext, List<GetAllFriends> listGetFriends) {
        this.mContext = mContext;
        this.listGetFriends = listGetFriends;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }*/

    public ChatListFriendsAdapter(Context mContext, List<ChatListModel> listGetFriends) {
        this.mContext = mContext;
        this.listGetFriends = listGetFriends;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    @NonNull
    @Override
    public ChatListFriendsAdapter.ViewHolerRoster onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chat_list_friends, parent, false);
        mContext = view.getContext();

        return new ChatListFriendsAdapter.ViewHolerRoster(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListFriendsAdapter.ViewHolerRoster holder, int position) {

        Picasso.get()
                .load(listGetFriends.get(position).getCustomerImagePath())
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(holder.ivPic);

        holder.tvUserName.setText(!((listGetFriends.get(position).getFname() + " " + listGetFriends.get(position).getLname()).equals("")) ?
                (listGetFriends.get(position).getFname() + " " + listGetFriends.get(position).getLname()) : "Not found");

        Log.e("CustomerId: ", "" + listGetFriends.get(position).getCustomerId());
        holder.btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnline()) {
                    pDialog = new ProgressDialog(mContext);
                    pDialog.setIndeterminate(true);
                    pDialog.setMessage("Loading Profile");
                    pDialog.show();
                    RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                    String url = "http://civiccare.net/api/AddOrRemoveFriend?mid=" + SPStaticUtils.getString("customerid")
                            + "&frid=" + listGetFriends.get(position).getCustomerId() + "&action=2";
                    Log.e("addFriend: ", url);
                    JsonObjectRequest movieReq = new JsonObjectRequest(url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject obj) {
                                    Log.d("addFriend--> ", obj.toString());
                                    hidePDialog();
                                    Toast.makeText(mContext, "Friend request send success", Toast.LENGTH_SHORT).show();
                                    try {
                                        JSONObject jsonObject = new JSONObject(obj.toString());
                                        strAddFriendRequest = jsonObject.getString("Result");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("", "Error: " + error.getMessage());
                            hidePDialog();
                        }
                    });
                    requestQueue.add(movieReq);
                }
            }
        });

//        holder.cvAddFriend.setOnClickListener(view -> {
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString(AppUrls.FRIEND_CustomerImagePath, listGetFriends.get(position).getCustomerImagePath());
//            editor.putString(AppUrls.FRIEND_fname, listGetFriends.get(position).getFname());
//            editor.putString(AppUrls.FRIEND_lname, listGetFriends.get(position).getLname());
//            editor.putString(AppUrls.FRIEND_EmailId, listGetFriends.get(position).getEmailId());
//            editor.putString(AppUrls.FRIEND_Mobile, listGetFriends.get(position).getMobile());
//            editor.putString(AppUrls.FRIEND_Profession, listGetFriends.get(position).getProfession());
//            editor.putString(AppUrls.FRIEND_ADDED_REQUEST_RESULT, strAddFriendRequest);
//            editor.apply();
//            Intent intent = new Intent(mContext, ChatListFriendDetailsActivity.class);
//            mContext.startActivity(intent);
//        });
    }

    @Override
    public int getItemCount() {
        return listGetFriends == null ? 0 : listGetFriends.size();
    }

    public class ViewHolerRoster extends RecyclerView.ViewHolder {

        private CardView cvAddFriend;
        private ImageView ivPic;
        private TextView tvUserName;
        private Button btnAddFriend;

        public ViewHolerRoster(@NonNull View itemView) {
            super(itemView);

            cvAddFriend = itemView.findViewById(R.id.cvAddFriend);
            ivPic = itemView.findViewById(R.id.ivPic);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            btnAddFriend = itemView.findViewById(R.id.btnAddFriend);

//            if (strAddFriendRequest.equalsIgnoreCase("1")) {
//                strAddFriendRequest = "1";
//            holder.btnAddFriend.setEnabled(false);
//            holder.btnAddFriend.setClickable(false);
//                Toast.makeText(mContext, "Already sent friend request", Toast.LENGTH_SHORT).show();
//            holder.btnAddFriend.setBackgroundResource(R.drawable.bg_btn_un_friend);
//            holder.btnAddFriend.setText("Unfriend");
//            } else {
//                strAddFriendRequest = "0";
//            holder.btnAddFriend.setEnabled(true);
//            holder.btnAddFriend.setClickable(true);
//            holder.btnAddFriend.setBackgroundResource(R.drawable.bg_btn_login);
//            holder.btnAddFriend.setText("Add Friend");

//            }
        }
    }

    public boolean isOnline() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) mContext.getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
}