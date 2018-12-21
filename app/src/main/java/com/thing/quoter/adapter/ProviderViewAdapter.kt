package com.thing.quoter.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.thing.quoter.FirestoreList
import com.thing.quoter.R
import com.thing.quoter.model.QuoteProvider
import kotlinx.android.synthetic.main.provider_item.view.*

class ProviderViewAdapter(val context: Context, val providers: FirestoreList<QuoteProvider>) : RecyclerView.Adapter<ProviderViewAdapter.ProviderViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ProviderViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.provider_item, p0, false)
        return ProviderViewHolder(view)
    }

    override fun getItemCount(): Int {
        return providers.size
    }

    override fun onBindViewHolder(p0: ProviderViewHolder, p1: Int) {
        p0.setIsRecyclable(false)
        val quoteProvider = providers.keyAt(p1)
        p0.bind(quoteProvider)
    }

    class ProviderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var providerImage: ImageView = itemView.providerImage
        val providerName: TextView = itemView.providerName
        fun bind(quoteProvider: QuoteProvider) {
            providerName.text = quoteProvider.providerName
//            Picasso.get()
//                    .load(R.drawable.ic_globe)
//                    .into(providerImage)
        }
    }
}