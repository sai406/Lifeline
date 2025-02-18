package com.mstech.lifeline.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mstech.lifeline.R
import com.mstech.lifeline.activities.ShowContactsActivity
import com.mstech.lifeline.databinding.SosLayoutBinding
import com.mstech.lifeline.models.SOScontactsItem


class SOSListAdapter(var context: Context, private val list: List<SOScontactsItem>) :
    RecyclerView.Adapter<SOSListAdapter.NoteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.sos_layout, parent, false)
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        with(holder) {
            var item = list[position]

            binding.sosname.text = item.name.toString()
            binding.sosno.text = item.mobile.toString()
            binding.emailid.text = item.emailId.toString()
            binding.delete.setOnClickListener(View.OnClickListener {
                val intent = Intent(ShowContactsActivity.RECEIVER_INTENT)
                intent.putExtra(ShowContactsActivity.RECEIVER_MESSAGE, item.contactId.toString())
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
            })

        }

    }

    class NoteViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val binding = SosLayoutBinding.bind(view)

    }


}

