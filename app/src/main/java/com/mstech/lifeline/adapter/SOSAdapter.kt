package com.mstech.lifeline.adapter

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mstech.lifeline.R
import com.mstech.lifeline.coordinater.activities.IncidentDetailsActivity
import com.mstech.lifeline.databinding.SosItemBinding
import com.mstech.lifeline.models.SOSResponse
import com.mstech.lifeline.models.SosInterface
import java.util.*


class SOSAdapter(
    var context: Context,
    private val list: List<SOSResponse>,
    var callback: SosInterface
) : RecyclerView.Adapter<SOSAdapter.NoteViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.incident_layout, parent, false)
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        with(holder) {
            var item = list[position]
            binding.place.setText(item.location)
            binding.name.setText(item.firstName + " " + item.lastName)
            binding.location.setText(getCompleteAddressString(item.latitude!!, item.longitude!!))
            Glide.with(context)  //2
                .load(item?.customerImagePath) //3
                .placeholder(R.drawable.ic_loading) //5
                .error(R.mipmap.ic_launcher) //6
                .into(binding.profileImage)
            holder.itemView.setOnClickListener(View.OnClickListener {
                context.startActivity(
                    Intent(context, IncidentDetailsActivity::class.java).putExtra(
                        "helpid",
                        item?.helpId.toString()
                    ).putExtra("from", "3")
                )
            })
            binding.notice.setOnClickListener(View.OnClickListener {
                val alertDialog: android.app.AlertDialog? =
                    android.app.AlertDialog.Builder(context).create()
                alertDialog!!.setTitle("Respond")
                alertDialog.setMessage("Are you Accept?")
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Decline",
                    DialogInterface.OnClickListener { dialog, which ->
                        callback.onClicked(
                            item.helpId!!,
                            0
                        )
                    })
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Accept",
                    DialogInterface.OnClickListener { dialog, which ->
                        callback.onClicked(
                            item.helpId!!,
                            1
                        )
                    })
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel",
                    DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
                alertDialog.show()

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
            }
        }


    }

    class NoteViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        public val binding = SosItemBinding.bind(view)

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