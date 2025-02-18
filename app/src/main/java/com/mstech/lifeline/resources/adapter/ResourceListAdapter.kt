package com.mstech.lifeline.resources.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mstech.lifeline.R
import com.mstech.lifeline.activities.YoutubePlayerActivity
import com.mstech.lifeline.databinding.ResourceListItemBinding
import com.mstech.lifeline.resources.activities.ResourcetActivity
import com.mstech.lifeline.resources.model.ResourceResponse
import com.mstech.lifeline.utils.WebViewWithNavigation


class ResourceListAdapter(
    var context: Context,
    private val list: List<ResourceResponse>,
    resourcesActivity: ResourcetActivity
) :
    RecyclerView.Adapter<ResourceListAdapter.NoteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.resource_list_item, parent, false)
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        with(holder) {
            var item = list[position]

            binding.docTitle.text = item.DocTitle.toString()
            binding.docBrief.text = item.ResourceBrief.toString()
            if (item.VideoUrl != null) {
                binding.playvideo.visibility = View.VISIBLE
                binding.playvideo.text = "Play Video"
            } else {
                binding.playvideo.visibility = View.GONE
            }
            binding.playvideo.setOnClickListener(View.OnClickListener {
                context.startActivity(
                    Intent(
                        context,
                        YoutubePlayerActivity::class.java
                    ).putExtra("youtube_link", item.VideoUrl)
                )

            })
            holder.itemView.setOnClickListener(View.OnClickListener {
//                context.startActivity(Intent(context,ResourceDetailsActivity::class .java).putExtra("data",item))
                context.startActivity(
                    Intent(
                        context,
                        WebViewWithNavigation::class.java
                    ).putExtra("url", item?.ResourceFilePath)
                )

            })

        }

    }

    class NoteViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val binding = ResourceListItemBinding.bind(view)

    }


}

