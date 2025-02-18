package com.mstech.lifeline.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mstech.lifeline.R
import com.mstech.lifeline.activities.CampaignDetailsActivity
import com.mstech.lifeline.models.NewCampaignsItem

import java.util.*

class CampainListAdapter(
    var context: Context,
    samplelist: List<NewCampaignsItem>,
    s: String
) :
    RecyclerView.Adapter<CampainListAdapter.MyViewHolder>(), Filterable {
    private val samplelist: MutableList<NewCampaignsItem>
    private var sampleFilterList: MutableList<NewCampaignsItem>
    private var type: String = ""

    init {
        sampleFilterList = samplelist as MutableList<NewCampaignsItem>
        type = s
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder { // infalte the item Layout
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.campaign_list_item, parent, false)
        // set the view's size, margins, paddings and layout parameters
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        val m: NewCampaignsItem = sampleFilterList[position]
        holder.campaignname.text = m.campaignTitle
        holder.campaigndate.text = m.datestring
        holder.campaignaddress.text = m.geoLocation
        Glide.with(context)  //2
            .load(m.imagePath) //3
            .placeholder(R.drawable.ic_loading) //5
            .error(R.drawable.logo) //6
            .fallback(R.drawable.logo) //7
            .into(holder.campaignimage)
        holder.itemView.setOnClickListener(View.OnClickListener {
//            context.startActivity(Intent(context,SwipeGameActivity::class.java).putExtra("gameid",m.gameId))
            context.startActivity(
                Intent(
                    context,
                    CampaignDetailsActivity::class.java
                ).putExtra("details", sampleFilterList[position]).putExtra("type", type)
            )

        })

    }

    override fun getItemCount(): Int {
        return sampleFilterList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty() || charSearch.equals("All")) {
                    sampleFilterList = samplelist
                } else {
                    val resultList = ArrayList<NewCampaignsItem>()
                    for (row in samplelist) {
                        if (row.campaignTitle?.toLowerCase(Locale.ROOT)!!.contains(
                                charSearch.toLowerCase(
                                    Locale.ROOT
                                )
                            ) || row.campaignInfo?.toString()?.toLowerCase(Locale.ROOT)!!.equals(
                                charSearch.toLowerCase(
                                    Locale.ROOT
                                )
                            )
                        ) {
                            resultList.add(row)
                        }
                    }
                    if (resultList != null) {
                        sampleFilterList = resultList
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = sampleFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                sampleFilterList = results?.values as MutableList<NewCampaignsItem>
                notifyDataSetChanged()
            }

        }
    }

    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var campaignimage: ImageView
        var campaignname: TextView
        var campaignaddress: TextView
        var campaigndate: TextView

        //
        init {
            // get the reference of item view's
            campaignimage = itemView.findViewById<View>(R.id.campaignimage) as ImageView
            campaignname = itemView.findViewById<View>(R.id.campaignname) as TextView
            campaigndate = itemView.findViewById<View>(R.id.starttime) as TextView
            campaignaddress = itemView.findViewById<View>(R.id.campaignaddress) as TextView
        }
    }

    init {
        this.samplelist = samplelist as MutableList<NewCampaignsItem>
    }

    fun updatesList(newlist: List<NewCampaignsItem> , tye:String) {
        sampleFilterList.clear()
        sampleFilterList.addAll(newlist)
        type = tye
        notifyDataSetChanged()
    }
}