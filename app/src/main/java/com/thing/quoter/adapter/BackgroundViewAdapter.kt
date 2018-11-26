package com.thing.quoter.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.thing.quoter.R
import kotlinx.android.synthetic.main.bg_thumbnail_item.view.*
import kotlinx.android.synthetic.main.provider_item.view.*

class BackgroundViewAdapter(val context: Context, val bgList: ArrayList<String>) : RecyclerView.Adapter<BackgroundViewAdapter.BackgroundViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): BackgroundViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.bg_thumbnail_item, p0, false)
        return BackgroundViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bgList.size
    }

    override fun onBindViewHolder(p0: BackgroundViewHolder, p1: Int) {
        p0.setIsRecyclable(false)
        val bgImageUri = bgList[p1]
        p0.bind(bgImageUri)
    }

    class BackgroundViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var thumbnail: ImageView = itemView.bgThumbnail
        fun bind(backgroundImageUri: String) {
            Picasso.get()
                    .load(backgroundImageUri)
                    .into(thumbnail)
        }
    }
}