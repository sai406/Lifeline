package com.mstech.lifeline.organisation.adapter

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mstech.lifeline.R
import com.mstech.lifeline.databinding.FileItemBinding
import com.mstech.lifeline.organisation.model.DocumentsItem
import com.mstech.lifeline.utils.WebViewWithNavigation



class DocumentsAdapter(var context: Context, private val list: List<DocumentsItem>) :
    RecyclerView.Adapter<DocumentsAdapter.NoteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.document_item, parent, false)
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        with(holder) {
            var item = list[position]
            item.documentPath?.let { binding.myWebView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + it) }

            holder.itemView.setOnClickListener(View.OnClickListener {
                context.startActivity(
                    Intent(
                        context,
                        WebViewWithNavigation::class.java
                    ).putExtra(
                        "url",
                        "http://docs.google.com/gview?embedded=true&url=" + item.documentPath
                    )
                )
            })

            binding.download.setOnClickListener(View.OnClickListener {
                val uri: Uri = Uri.parse(item.documentPath)
                val downloadManager =
                    context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
                val request = DownloadManager.Request(uri)
                request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI or
                            DownloadManager.Request.NETWORK_MOBILE
                )

                request.setTitle("Download")
                request.setDescription("File downloading ...")

                request.allowScanningByMediaScanner()
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

                request.setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    item.document + ".pdf"
                )
                request.setMimeType("*/*")
                Toast.makeText(context,"Downling check in Notification",Toast.LENGTH_LONG).show()
                downloadManager!!.enqueue(request)
            })

        }

    }

    class NoteViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val binding = FileItemBinding.bind(view)

    }
}


