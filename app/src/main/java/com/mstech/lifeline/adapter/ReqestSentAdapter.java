package com.mstech.lifeline.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mstech.lifeline.R;
import com.mstech.lifeline.models.ReqestPendingModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * HARISH GADDAM
 */

public class ReqestSentAdapter extends RecyclerView.Adapter<ReqestSentAdapter.ViewHolerRoster> {

    Context mContext;
    private List<ReqestPendingModel> listRequestSent = new ArrayList<ReqestPendingModel>();

    public ReqestSentAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public ReqestSentAdapter(Context mContext, List<ReqestPendingModel> listRequestSent) {
        this.mContext = mContext;
        this.listRequestSent = listRequestSent;
    }

    @NonNull
    @Override
    public ReqestSentAdapter.ViewHolerRoster onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_request_pending_sent, parent, false);
        mContext = view.getContext();

        return new ReqestSentAdapter.ViewHolerRoster(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReqestSentAdapter.ViewHolerRoster holder, int position) {

        Log.e("", "");
        holder.btnAccept.setVisibility(View.GONE);
        holder.btnIgnore.setVisibility(View.GONE);
        Picasso.get()
                .load(listRequestSent.get(position).getCustomerImagePath())
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(holder.ivPic);

        holder.tvUserName.setText(!((listRequestSent.get(position).getFirstName() + " " + listRequestSent.get(position).getLastName()).equals("")) ?
                (listRequestSent.get(position).getFirstName() + " " + listRequestSent.get(position).getLastName()) : "Not found");
        holder.tvRequestStatus.setText(!((listRequestSent.get(position).getRequestStatus()).equals("")) ?
                (listRequestSent.get(position).getRequestStatus()) : "Not found");
        holder.cvRequestPendingSent.setOnClickListener(view -> {
        });
    }

    @Override
    public int getItemCount() {
        return listRequestSent == null ? 0 : listRequestSent.size();
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
        }
    }
}
