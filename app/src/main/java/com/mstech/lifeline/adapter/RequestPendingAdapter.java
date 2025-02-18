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
import com.mstech.lifeline.activities.NotificationActivity;
import com.mstech.lifeline.models.ReqestPendingModel;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * HARISH GADDAM
 */

public class RequestPendingAdapter extends RecyclerView.Adapter<RequestPendingAdapter.ViewHolerRoster> {

    Context mContext;
    private List<ReqestPendingModel> listRequestPending = new ArrayList<ReqestPendingModel>();
    private ProgressDialog pDialog;
    SharedPreferences sharedPreferences;

    public RequestPendingAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public RequestPendingAdapter(Context mContext, List<ReqestPendingModel> listRequestPending) {
        this.mContext = mContext;
        this.listRequestPending = listRequestPending;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    @NonNull
    @Override
    public RequestPendingAdapter.ViewHolerRoster onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_request_pending_sent, parent, false);
        mContext = view.getContext();

        return new RequestPendingAdapter.ViewHolerRoster(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestPendingAdapter.ViewHolerRoster holder, int position) {

        Picasso.get()
                .load(listRequestPending.get(position).getCustomerImagePath())
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(holder.ivPic);

        holder.tvUserName.setText(!((listRequestPending.get(position).getFirstName() + " " + listRequestPending.get(position).getLastName()).equals("")) ?
                (listRequestPending.get(position).getFirstName() + " " + listRequestPending.get(position).getLastName()) : "Not found");
        holder.tvRequestStatus.setText(!((listRequestPending.get(position).getRequestStatus()).equals("")) ?
                (listRequestPending.get(position).getRequestStatus()) : "Not found");
    }

    @Override
    public int getItemCount() {
        return listRequestPending == null ? 0 : listRequestPending.size();
    }

    public class ViewHolerRoster extends RecyclerView.ViewHolder {

        private CardView cvRequestPendingSent;
        private ImageView ivPic;
        private TextView tvUserName;
        private TextView tvRequestStatus;
        private Button btnAccept;
        private Button btnIgnore;

        public ViewHolerRoster(@NonNull View itemView) {
            super(itemView);

            cvRequestPendingSent = itemView.findViewById(R.id.cvRequestPendingSent);
            ivPic = itemView.findViewById(R.id.ivPic);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvRequestStatus = itemView.findViewById(R.id.tvRequestStatus);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnIgnore = itemView.findViewById(R.id.btnIgnore);

            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isOnline()) {
                        pDialog = new ProgressDialog(mContext);
                        pDialog.setIndeterminate(true);
                        pDialog.setMessage("Loading ...");
                        pDialog.show();

                        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                        String url = "http://civiccare.net/api/AddOrRemoveFriend?mid=" + SPStaticUtils.getString("customerid") +
                                "&frid=" + listRequestPending.get(getAdapterPosition()).getCustomerId() + "&action=1";
                        Log.e("acceptFriend: ", url);
                        JsonObjectRequest movieReq = new JsonObjectRequest(url, null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject obj) {
                                        hidePDialog();
                                        Log.d("acceptFriend--> ", obj.toString());
                                        // D/acceptFriend-->: {"Result":1}

                                        if (obj.has("Result")) {
                                            try {
                                                if (obj.getString("Result").equalsIgnoreCase("1")) {
                                                    Toast.makeText(mContext, "Friend Accepted Success", Toast.LENGTH_SHORT).show();
                                                    mContext.startActivity(new Intent(mContext, NotificationActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                                } else {
                                                    Toast.makeText(mContext, "Please try again later ...", Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

//                                        removeItem(getAdapterPosition());

//                                        notifyDataSetChanged();
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

            btnIgnore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isOnline()) {
                        pDialog = new ProgressDialog(mContext);
                        pDialog.setIndeterminate(true);
                        pDialog.setMessage("Loading ...");
                        pDialog.show();

                        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                        String url = "http://civiccare.net/api/AddOrRemoveFriend?mid=" + SPStaticUtils.getString("customerid") +
                                "&frid=" + listRequestPending.get(getAdapterPosition()).getCustomerId() + "&action=0";
                        Log.e("ignore: ", url);
                        JsonObjectRequest movieReq = new JsonObjectRequest(url, null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject obj) {
                                        Log.d("ignoreFriend--> ", obj.toString());
                                        hidePDialog();

                                        if (obj.has("Result")) {
                                            try {
                                                if (obj.getString("Result").equalsIgnoreCase("1")) {
                                                    Toast.makeText(mContext, "Friend Ignore Success", Toast.LENGTH_SHORT).show();
                                                    mContext.startActivity(new Intent(mContext, NotificationActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                                } else {
                                                    Toast.makeText(mContext, "Please try again later ...", Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        // here removed particular cell based on button click
                                        /*removeItem(getAdapterPosition());
                                        Toast.makeText(mContext, "Friend Ignore Success", Toast.LENGTH_SHORT).show();
                                        ((NotificationActivity)mContext).getAllRequestListFromServer();
//                                        mContext.startActivity(new Intent(mContext, NotificationActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                        notifyDataSetChanged();*/
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
        }
    }

    public void removeItem(int position) {
        listRequestPending.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
        ((NotificationActivity)mContext).getAllRequestListFromServer();
        // Add whatever you want to do when removing an Item
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
