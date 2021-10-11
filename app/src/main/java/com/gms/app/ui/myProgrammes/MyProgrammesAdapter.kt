package com.gms.app.ui.myProgrammes

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gms.app.R
import com.gms.app.data.storage.remote.model.programs.MyProgrammeModel
import kotlin.collections.ArrayList

class MyProgrammesAdapter internal constructor(
    private val mContext: Context,
) : RecyclerView.Adapter<MyProgrammesAdapter.AppViewHolder>() {

    private var mData: ArrayList<MyProgrammeModel> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return AppViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.my_programme_item_view, parent, false)
        )
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val model = mData[position]
        holder.programmePeriod.text = model.period
        holder.programmeDate.text = model.startDate
        holder.programmeHours.text =
            model.numberOfHours + " " + mContext.resources.getString(R.string.hours)
        holder.programmeSets.text =
            model.numberOfSeats + " " + mContext.resources.getString(R.string.sets)
        holder.programmeTitle.text = model.title

    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun renderData(it: ArrayList<MyProgrammeModel>) {
        mData.addAll(it)
        notifyDataSetChanged()
    }

    class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val programmePeriod = itemView.findViewById<TextView>(R.id.programme_period_txt)!!
        val programmeDate = itemView.findViewById<TextView>(R.id.programme_date_txt)!!
        val programmeHours = itemView.findViewById<TextView>(R.id.programme_hours_txt)!!
        val programmeSets = itemView.findViewById<TextView>(R.id.programme_sets_txt)!!
        val programmeTitle = itemView.findViewById<TextView>(R.id.programme_title_txt)!!

    }


}