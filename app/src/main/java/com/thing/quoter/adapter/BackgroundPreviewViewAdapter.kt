package com.thing.quoter.adapter

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.thing.quoter.R
import kotlinx.android.synthetic.main.background_preview_item.view.*

class BackgroundPreviewViewAdapter(val context: Context, val backgrounds: Set<String>) : RecyclerView.Adapter<BackgroundPreviewViewAdapter.BackgroundViewHolder>() {

    var listener: ((String) -> Unit)? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): BackgroundViewHolder {
        return BackgroundViewHolder(LayoutInflater.from(context).inflate(R.layout.background_preview_item, p0, false))
    }

    override fun getItemCount(): Int {
        return backgrounds.size
    }


    override fun onBindViewHolder(p0: BackgroundViewHolder, p1: Int) {
        p0.setIsRecyclable(false)
        p0.bind(backgrounds.elementAt(p1))
        p0.itemView.setOnClickListener { listener?.invoke(backgrounds.elementAt(p1)) }
    }

    class BackgroundViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var backgroundPreviewImageView: ImageView = itemView.backgroundPreview

        fun bind(backgroundRes: String) {
            Picasso.get()
                    .load(backgroundRes)
                    .placeholder(R.drawable.bg_preview_item_placeholder)
                    .error(android.R.color.black)
                    .into(backgroundPreviewImageView)
        }
    }
}