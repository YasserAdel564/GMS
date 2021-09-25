package com.gms.app.ui.main.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filterable
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.gms.app.R
import com.gms.app.utils.TestList
import kotlin.collections.ArrayList

class DepartmentsAdapter internal constructor(
    mContext: Context,
    apps: ArrayList<TestList.TestItem>
) : RecyclerView.Adapter<DepartmentsAdapter.AppViewHolder>() {


    private val mContext: Context = mContext
    private var mData: ArrayList<TestList.TestItem> = apps

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return AppViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.department_item_view, parent, false)
        )
    }


    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.depName.text = mContext.resources.getString(R.string.program) + " " + position + 1
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
    }


}