package com.mstech.lifeline.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mstech.lifeline.R
import com.mstech.lifeline.activities.VideoPlayerActivity
import com.mstech.lifeline.databinding.GalleryItemBinding
import java.io.File


class GalleryAdapter(
    var context: Context,
    private val list: Array<File>?,
) : RecyclerView.Adapter<GalleryAdapter.NoteViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.gallery_item, parent, false)
        )
    }

    override fun getItemCount() = list!!.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        with(holder) {
            var item = list?.get(position)
            binding.name.setText(item?.name!!.split("_")[0]+" "+item?.name!!.split("_")[1])
            Glide.with(context)  //2
                .load(item?.absolutePath) //3
                .placeholder(R.drawable.ic_loading) //5
                .error(R.mipmap.ic_launcher) //6
                .into(binding.thumbnail)
            holder.itemView.setOnClickListener(View.OnClickListener {
                val videoFile = File(item.absolutePath)
                val fileUri = FileProvider.getUriForFile(
                    context,
                    "com.mstech.lifeline.fileprovider",
                    videoFile)
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(fileUri, "video/*")
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) //DO NOT FORGET THIS EVER
                context.startActivity(intent)

//                val intent = Intent(context,VideoPlayerActivity::class.java)
//                intent.putExtra("path", item.absolutePath)
//                context.startActivity(intent)
            })
        }

    }
    class NoteViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        public val binding = GalleryItemBinding.bind(view)

    }


}