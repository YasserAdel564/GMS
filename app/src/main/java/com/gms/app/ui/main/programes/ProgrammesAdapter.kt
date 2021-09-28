package com.gms.app.ui.main.programes

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
import com.gms.app.data.storage.remote.model.programs.ProgrammeModel
import kotlin.collections.ArrayList

class ProgrammesAdapter internal constructor(
    private val mContext: Context,
    private val programmeCallback: ProgrammeCallback
) : RecyclerView.Adapter<ProgrammesAdapter.AppViewHolder>() {

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
        holder.depItem.setOnClickListener {
            programmeCallback.programmeClicked(model)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun renderData(it: ArrayList<ProgrammeModel>) {
        mData.addAll(it)
        notifyDataSetChanged()
    }

    class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val depItem = itemView.findViewById<LinearLayout>(R.id.dep_item)!!
        val depName = itemView.findViewById<TextView>(R.id.dep_name)!!
        val depImage = itemView.findViewById<ImageView>(R.id.dep_img)!!
    }

    interface ProgrammeCallback {
        fun programmeClicked(model: ProgrammeModel?)
    }

}