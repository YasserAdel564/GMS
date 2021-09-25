package com.gms.app.ui.main.programes

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gms.app.R
import com.gms.app.utils.TestList
import kotlin.collections.ArrayList

class ProgramsAdapter internal constructor(
    mContext: Context,
    apps: ArrayList<TestList.TestItem>
) : RecyclerView.Adapter<ProgramsAdapter.AppViewHolder>() {


    private val mContext: Context = mContext
    private var mData: ArrayList<TestList.TestItem> = apps

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return AppViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.department_item_view, parent, false)
        )
    }


    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.depName.text = mContext.resources.getString(R.string.department) + " " + position + 1
        Glide.with(mContext)
            .load("https://images.indianexpress.com/2020/04/online759.jpg")
            .into(holder.depImage)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun renderData(it: ArrayList<TestList.TestItem>) {
        mData = it
        notifyDataSetChanged()
    }

    fun updateItem(item: TestList.TestItem, position: Int) {
        mData[position] = item
        mData[position] = item
        notifyItemChanged(position)
    }

    class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val depName = itemView.findViewById<TextView>(R.id.dep_name)
        val depImage = itemView.findViewById<ImageView>(R.id.dep_img)
    }


}