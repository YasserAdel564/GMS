package com.gms.app.ui.main.programes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gms.app.R
import com.gms.app.data.storage.remote.model.programs.ProgrammeModel
import kotlin.collections.ArrayList

class ProgramsAdapter internal constructor(
    private val mContext: Context
) : RecyclerView.Adapter<ProgramsAdapter.AppViewHolder>() {


    private var mData: ArrayList<ProgrammeModel> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return AppViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.department_item_view, parent, false)
        )
    }


    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val model = mData[position]
        holder.depName.text = model.title
        Glide.with(mContext)
            .load(model.img)
            .into(holder.depImage)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun renderData(it: ArrayList<ProgrammeModel>) {
        mData.addAll(it)
        notifyDataSetChanged()
    }

    fun updateItem(item: ProgrammeModel, position: Int) {
        mData[position] = item
        mData[position] = item
        notifyItemChanged(position)
    }

    class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val depName = itemView.findViewById<TextView>(R.id.dep_name)
        val depImage = itemView.findViewById<ImageView>(R.id.dep_img)
    }


}