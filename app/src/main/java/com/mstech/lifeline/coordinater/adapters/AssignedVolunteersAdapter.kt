package com.mstech.lifeline.coordinater.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mstech.lifeline.R
import com.mstech.lifeline.coordinater.activities.IncidentDetailsActivity
import com.mstech.lifeline.coordinater.model.AssignedVolunteerResponse
import com.mstech.lifeline.databinding.AssignedVolunteerBinding


class AssignedVolunteersAdapter(
    var context: Context,
    private val list: List<AssignedVolunteerResponse>
) :
    RecyclerView.Adapter<AssignedVolunteersAdapter.NoteViewHolder>() {
    val filter = arrayListOf<Int>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.assigned_volunteer, parent, false)
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        with(holder) {
            var item = list[position]
            binding.name.setText(item.FirstName + " " + item.LastName)
            binding.location.setText(item.GeoAddress)
            Glide.with(context)  //2
                .load(item?.CustomerImagePath) //3
                .placeholder(R.drawable.ic_loading) //5
                .error(R.mipmap.ic_launcher) //6
                .into(binding.profileImage)
            binding.phone.setText(item.Mobile)
            binding.phone.setOnClickListener(View.OnClickListener {
                val u: Uri = Uri.parse("tel:${item.Mobile}")
                val i = Intent(Intent.ACTION_VIEW, u)
                context.startActivity(i)
            })

        }

    }

    class NoteViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val binding = AssignedVolunteerBinding.bind(view)

    }


}

