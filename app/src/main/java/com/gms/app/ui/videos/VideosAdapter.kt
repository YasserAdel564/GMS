package com.gms.app.ui.videos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gms.app.R
import com.gms.app.data.storage.remote.model.video.VideoModel
import kotlin.collections.ArrayList

class VideosAdapter internal constructor(
    private val mContext: Context,
    private val videoCallback: VideoCallback
) : RecyclerView.Adapter<VideosAdapter.AppViewHolder>() {

    private var mData: ArrayList<VideoModel> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return AppViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.video_item_view, parent, false)
        )
    }


    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val model = mData[position]
        Glide.with(mContext)
            .load(model.img)
            .into(holder.videoImage)
        holder.videoSummary.text = model.brief
        holder.videoTitle.text = model.title
        holder.videoDate.text = model.date
        holder.videoItem.setOnClickListener {
            videoCallback.videoClicked(model)
        }

    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun renderData(it: ArrayList<VideoModel>) {
        mData.addAll(it)
        notifyDataSetChanged()
    }

    class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val videoItem = itemView.findViewById<CardView>(R.id.video_item)!!
        val videoImage = itemView.findViewById<ImageView>(R.id.video_img)!!
        val videoSummary = itemView.findViewById<TextView>(R.id.video_summary_txt)!!
        val videoDate = itemView.findViewById<TextView>(R.id.video_date_txt)!!
        val videoTitle = itemView.findViewById<TextView>(R.id.video_title_txt)!!


    }
    interface VideoCallback {
        fun videoClicked(model: VideoModel?)
    }

}