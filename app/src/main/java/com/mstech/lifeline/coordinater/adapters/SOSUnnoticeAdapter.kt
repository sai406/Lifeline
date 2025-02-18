package com.mstech.lifeline.coordinater.adapters

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mstech.lifeline.R
import com.mstech.lifeline.coordinater.activities.IncidentDetailsActivity
import com.mstech.lifeline.coordinater.activities.IncidentListActivity
import com.mstech.lifeline.coordinater.model.LatestItem
import com.mstech.lifeline.coordinater.model.RescueAction
import com.mstech.lifeline.databinding.IncidentLayoutBinding
import com.mstech.lifeline.databinding.SosItemBinding
import java.util.*


class SOSUnnoticeAdapter(
    var context: Context,
    private val list: List<LatestItem>,
    val action: IncidentListActivity
) : RecyclerView.Adapter<SOSUnnoticeAdapter.NoteViewHolder>() {
    private var mAdapterCallback: AdapterCallback? = null
    var dtInterface: RescueAction? = null


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
//            binding.location.setText(item.geoAddress.toString())
            binding.date.text = item.createDatestring
            Glide.with(context)  //2
                .load(item?.customerImagePath) //3
                .placeholder(R.drawable.ic_loading) //5
                .error(R.mipmap.ic_launcher) //6
                .into(binding.profileImage)
            holder.itemView.setOnClickListener(View.OnClickListener {
                context.startActivity(
                    Intent(context, IncidentDetailsActivity::class.java).putExtra(
                        "helpid",
                        item.helpId.toString()
                    ).putExtra("from","1")
                )
            })
            binding.notice.setOnClickListener(View.OnClickListener {
                val alertDialogBuilder: android.app.AlertDialog.Builder =
                    android.app.AlertDialog.Builder(context)
                alertDialogBuilder.setTitle("Confirmation")
                alertDialogBuilder
                    .setMessage("Assign to Volunteer!")
                    .setCancelable(false)
                    .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->

                        action.rescueBtn(item.helpId!!)
                    })
                    .setNegativeButton(
                        "No",
                        DialogInterface.OnClickListener { dialog, id -> // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel()
                        })
                val alertDialog: android.app.AlertDialog? = alertDialogBuilder.create()
                alertDialog!!.show()
//                context.startActivity(Intent(context,VolunteersListActivity::class.java).putExtra("id",item.memberId.toString()))
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
        public val binding = IncidentLayoutBinding.bind(view)

    }

    interface AdapterCallback {
        fun onMethodCallback(helpId: Int?)
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