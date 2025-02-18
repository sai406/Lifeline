package com.mstech.lifeline.coordinater.adapters

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mstech.lifeline.R
import com.mstech.lifeline.coordinater.activities.IncidentDetailsActivity
import com.mstech.lifeline.coordinater.activities.UnnoticedIncidentDetailsActivity
import com.mstech.lifeline.coordinater.model.NoticedItem
import com.mstech.lifeline.databinding.IncidentLayoutBinding
import com.mstech.lifeline.databinding.SosItemBinding

import java.util.*

class SOSAdapter(var context: Context, private val list: List<NoticedItem>) : RecyclerView.Adapter<SOSAdapter.NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.incident_layout, parent, false)
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        with(holder) {
            val item = list[position]
           binding.place.setText(item.location)
           binding.name.setText(item.firstName + " " + item.lastName)
            binding.location.setText(getCompleteAddressString(item.latitude!!,item.longitude!!))
            binding.notice.visibility =View.GONE
            Glide.with(context)  //2
                .load(item?.customerImagePath) //3
                .placeholder(R.drawable.ic_loading) //5
                .error(R.mipmap.ic_launcher) //6
                .into(binding.profileImage)
            binding.date.text = item.createDatestring
            holder.itemView.setOnClickListener(View.OnClickListener {
                context.startActivity(
                    Intent(context, UnnoticedIncidentDetailsActivity::class.java).putExtra(
                        "helpid",
                        item.helpId.toString()
                    ).putExtra("from","2")
                )
            })
            binding.map.setOnClickListener {
                try {
                    val uri = String.format(
                        Locale.ENGLISH,
                        "http://maps.google.com/maps?q=loc:%f,%f",
                        item.latitude,
                        item.longitude
                    )
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                    context.startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }}

    }
    class NoteViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        public val binding = IncidentLayoutBinding.bind(view)

    }

    private fun getCompleteAddressString(LATITUDE: Double, LONGITUDE: Double): String {
        var strAdd = ""
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses: List<Address>? = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1)
            if (addresses != null) {
                val returnedAddress: Address = addresses[0]
                val strReturnedAddress = StringBuilder("")
                for (i in 0..returnedAddress.getMaxAddressLineIndex()) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                }
                strAdd = strReturnedAddress.toString()

            } else {

            }
        } catch (e: Exception) {
            e.printStackTrace()

        }
        return strAdd
    }

}