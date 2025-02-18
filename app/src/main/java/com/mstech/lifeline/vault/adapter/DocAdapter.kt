package com.mstech.lifeline.vault.adapter

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SPStaticUtils
import com.bumptech.glide.Glide
import com.mstech.lifeline.R
import com.mstech.lifeline.activities.LoginActivity
import com.mstech.lifeline.coordinater.activities.IncidentListActivity
import com.mstech.lifeline.databinding.GalleryItemBinding
import com.mstech.lifeline.utils.WebViewWithNavigation
import com.mstech.lifeline.vault.activities.DocListActivity
import com.mstech.lifeline.vault.activities.PdfViewerActivity
import kotlinx.coroutines.launch
import java.io.File


class DocAdapter(
    var context: Context,
    private val list: Array<File>?,
    private val type: String,
    val action: DocListActivity
) : RecyclerView.Adapter<DocAdapter.NoteViewHolder>() {


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
            binding.name.setText(item?.name!!.split("_")[0])
            if(type.equals("1")){
                Glide.with(context)  //2
                    .load(item?.absolutePath) //3
                    .placeholder(R.drawable.ic_loading) //5
                    .error(R.drawable.ic_loading) //6
                    .centerInside()
                    .into(binding.thumbnail)
            }else{
                Glide.with(context)  //2
                    .load(context.resources.getDrawable(R.drawable.pdficon)) //3
                    .placeholder(R.drawable.ic_loading) //5
                    .error(R.mipmap.ic_launcher) //6
                    .centerInside()
                    .into(binding.thumbnail)
            }

            holder.itemView.setOnClickListener(View.OnClickListener {
                if (type=="1"){
                    context.startActivity(Intent(context, WebViewWithNavigation::class.java).putExtra("url","file:///"+item?.absolutePath))
                }else{
                    context.startActivity(Intent(context,PdfViewerActivity::class.java).putExtra("url",item?.absolutePath))

                }
            })
            //Long Press
            //Long Press
            holder.itemView.setOnLongClickListener(OnLongClickListener { v ->
                if (item != null) {
                    showDialog("Are you sure want to Delete?", "", context,item)
                }
                false
            })
        }

    }
    class NoteViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        public val binding = GalleryItemBinding.bind(view)

    }
    fun showDialog(title: String?, Message: String?, context: Context , item : File)  {
        val builder = android.app.AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setIcon(context.resources.getDrawable(R.mipmap.ic_launcher))
        builder.setCancelable(true)
        builder.setMessage(Message)
        val positiveText = context.getString(android.R.string.ok)
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, which -> //opration do here on Click "Close"
            dialog.dismiss()
        }
        builder.setPositiveButton(
            "OK"
        ) { dialog, which -> //opration do here on Click "Close"
            File(item.absolutePath).delete()
            dialog.dismiss()
            action.refresh()
        }
        val dialog = builder.create()
        // display dialog
        dialog.show()
    }


}