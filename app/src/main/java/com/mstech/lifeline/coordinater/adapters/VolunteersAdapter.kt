package com.mstech.lifeline.coordinater.adapters

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
import com.mstech.lifeline.coordinater.model.VolunteerListResponse
import com.mstech.lifeline.databinding.VolunteerItemBinding
import com.mstech.lifelinecoordinator.activities.VolunteersListActivity



class VolunteersAdapter(var context: Context, private val list: List<VolunteerListResponse>) :
    RecyclerView.Adapter<VolunteersAdapter.NoteViewHolder>() {
    val filter = arrayListOf<Int>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.volunteer_item, parent, false)
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        with(holder) {
            var item = list[position]

            binding.chid.text = item.firstName + " " + item.lastName

            Glide.with(context)  //2
                .load(item.profilePic) //3
                .placeholder(R.drawable.ic_loading) //5
                .error(R.mipmap.ic_launcher) //6
                .into(binding.image)
            binding.chid.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    filter?.add(item.memberId!!)
                    val intent = Intent(VolunteersListActivity.RECEIVER_INTENT)
                    intent.putExtra(VolunteersListActivity.RECEIVER_MESSAGE, filter.toString())
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
                } else {
                    filter?.remove(item.memberId)
                    val intent = Intent(VolunteersListActivity.RECEIVER_INTENT)
                    intent.putExtra(VolunteersListActivity.RECEIVER_MESSAGE, filter.toString())
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
                }

            })

        }

    }

    class NoteViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val binding = VolunteerItemBinding.bind(view)

    }


}

