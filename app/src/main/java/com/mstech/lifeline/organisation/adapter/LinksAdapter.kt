package com.mstech.lifeline.organisation.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mstech.lifeline.R
import com.mstech.lifeline.databinding.FileItemBinding
import com.mstech.lifeline.organisation.model.LinksItem
import com.mstech.lifeline.utils.WebViewWithNavigation


class LinksAdapter(var context: Context, private val list: List<LinksItem>) :
        RecyclerView.Adapter<LinksAdapter.NoteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.file_item, parent, false)
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        with(holder) {
            var item = list[position]

            binding.myWebView.load(item.link);

            holder.itemView.setOnClickListener(View.OnClickListener {
                context.startActivity(Intent(context, WebViewWithNavigation::class.java).putExtra("url",item.link))
            })

        }

    }

    class NoteViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val binding = FileItemBinding.bind(view)

    }


}