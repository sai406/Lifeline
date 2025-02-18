package com.mstech.lifeline.organisation.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mstech.lifeine.organisation.model.OrganisationResponse
import com.mstech.lifeline.R
import com.mstech.lifeline.databinding.OrganisationItemBinding
import com.mstech.lifeline.organisation.activities.ArticlesListActivity


class OrganisationListAdapter(var context: Context, private val list: List<OrganisationResponse>) :
        RecyclerView.Adapter<OrganisationListAdapter.NoteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.organisation_item, parent, false)
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        with(holder) {
            var item = list[position]

            binding.organisationname.text = item.organisation.toString()

            holder.itemView.setOnClickListener(View.OnClickListener {
  context.startActivity(Intent(context, ArticlesListActivity::class.java).putExtra("orgId",item.organisationId.toString()))
            })

        }

    }

    class NoteViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val binding = OrganisationItemBinding.bind(view)

    }


}

