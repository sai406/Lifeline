package com.mstech.lifeline.coordinater.adapters

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
import com.mstech.lifeline.coordinater.activities.MemberDetailsActivity
import com.mstech.lifeline.coordinater.model.MemberResponse
import layout.CoordinatorMemberResponse
import java.util.*

class MembersAdapter(
    var context: Context,
    samplelist: List<CoordinatorMemberResponse>
) :
    RecyclerView.Adapter<MembersAdapter.MyViewHolder>(), Filterable {
    private val samplelist: MutableList<CoordinatorMemberResponse>
    private var sampleFilterList: MutableList<CoordinatorMemberResponse>

    init {
        sampleFilterList = samplelist as MutableList<CoordinatorMemberResponse>
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder { // infalte the item Layout
        val v: View = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.member_layout, parent, false)
        // set the view's size, margins, paddings and layout parameters
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        val m: CoordinatorMemberResponse = sampleFilterList[position]
        holder.name.text = m.FirstName+" "+m.LastName
        holder.number.text = m.Mobile
        Glide.with(context)  //2
            .load(m.CustomerImagePath) //3
            .placeholder(R.drawable.ic_loading) //5
            .error(R.drawable.ic_loading) //6
            .fallback(R.drawable.ic_loading) //7
            .into(holder.image)
        holder.itemView.setOnClickListener(View.OnClickListener {
//            context.startActivity(Intent(context,SwipeGameActivity::class.java).putExtra("gameid",m.gameId))
            context.startActivity(
                Intent(
                    context,
                    MemberDetailsActivity::class.java
                ).putExtra("data", sampleFilterList[position])
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
                    val resultList = ArrayList<CoordinatorMemberResponse>()
                    for (row in samplelist) {
                        if (row.FirstName?.toLowerCase(Locale.ROOT)!!.contains(
                                charSearch.toLowerCase(
                                    Locale.ROOT
                                )
                            ) || row.LastName?.toString()?.toLowerCase(Locale.ROOT)!!.equals(
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
                sampleFilterList = results?.values as MutableList<CoordinatorMemberResponse>
                notifyDataSetChanged()
            }

        }
    }

    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var image: ImageView
        var name: TextView
        var number: TextView


        //
        init {
            // get the reference of item view's
            image = itemView.findViewById<View>(R.id.thumbnail) as ImageView
            name = itemView.findViewById<View>(R.id.name) as TextView
            number = itemView.findViewById<View>(R.id.mobile) as TextView

        }
    }

    init {
        this.samplelist = samplelist as MutableList<CoordinatorMemberResponse>
    }
}