package com.thing.quoter.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.thing.quoter.R
import com.thing.quoter.databinding.BackgroundPreviewItemBinding

class BackgroundPreviewViewAdapter(val context: Context, private val backgrounds: Set<String>) : RecyclerView.Adapter<BackgroundPreviewViewAdapter.BackgroundViewHolder>() {

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
        private val backgroundPreviewImageView: ImageView = itemView.findViewById(R.id.backgroundPreview)

        fun bind(backgroundRes: String) {
            Log.i("TF", backgroundRes);
            Picasso.get()
                    .load(backgroundRes)
                    .placeholder(R.drawable.bg_preview_item_placeholder)
                    .error(android.R.color.black)
                    .into(backgroundPreviewImageView)
        }
    }
}