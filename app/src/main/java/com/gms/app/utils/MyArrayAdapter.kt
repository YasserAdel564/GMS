package com.gms.app.utils

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView


class MyArrayAdapter<T>(
    context: Context?, items: List<T?>?,
    private var firstPositionDesabled: Boolean
) :
    ArrayAdapter<T?>(context!!, android.R.layout.simple_spinner_item, items!!) {

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val tv = view as TextView
        if (position == 0 && firstPositionDesabled)
            tv.setTextColor(Color.GRAY)
         else
            tv.setTextColor(Color.BLACK)

        return view
    }

    override fun isEnabled(position: Int): Boolean {
        return if (firstPositionDesabled) position != 0 else super.isEnabled(position)
    }
}