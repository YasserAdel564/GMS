package com.gms.app.ui.main.home

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.gms.app.R
import com.gms.app.data.storage.remote.model.home.SliderModel


class SliderAdapter : PagerAdapter() {

    private val slider = ArrayList<SliderModel>()

    override fun isViewFromObject(view: View, imgv: Any): Boolean {
        return view == imgv as ConstraintLayout
    }

    override fun getCount(): Int {
        return slider.size
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        val onScreenView: View = `object` as View
        val image: ImageView = onScreenView.findViewById(R.id.slider_img)
        super.setPrimaryItem(container, position, `object`)
    }

    @SuppressLint("SetTextI18n")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context)
            .inflate(R.layout.slider_item, container, false) as ConstraintLayout
        val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
        container.addView(view)

        val image: ImageView = view.findViewById(R.id.slider_img)
        val title = view.findViewById<TextView>(R.id.slider_txt)


        val options: RequestOptions = RequestOptions()
            .centerInside()
            .placeholder(R.drawable.app_logo)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH)
        Glide.with(container.context)
            .load(slider[position].slice)
            .apply(options)
            .into(image)
        title.text = slider[position].headline + "\n" + slider[position].paragraph
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    fun submitList(imagesList: ArrayList<SliderModel>) {
        slider.clear()
        slider.addAll(imagesList)
        notifyDataSetChanged()
    }


}
