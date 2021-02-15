package com.thing.quoter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.thing.quoter.R
import com.thing.quoter.databinding.ProviderItemBinding
import com.thing.quoter.helper.FirestoreList
import com.thing.quoter.repository.model.QuoteProvider

class ProviderViewAdapter(val context: Context, private val providers: FirestoreList<QuoteProvider>) : RecyclerView.Adapter<ProviderViewAdapter.ProviderViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ProviderViewHolder {
        val binding = ProviderItemBinding.inflate(LayoutInflater.from(context), p0, false)
        return ProviderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return providers.size
    }

    override fun onBindViewHolder(p0: ProviderViewHolder, p1: Int) {
        p0.setIsRecyclable(false)
        val quoteProvider = providers.keyAt(p1)
        p0.bind(quoteProvider)
    }

    class ProviderViewHolder(val itemBinding: ProviderItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        var providerImage: ImageView = itemBinding.providerImage
        val providerName: TextView = itemBinding.providerName
        fun bind(quoteProvider: QuoteProvider) {
            providerName.text = quoteProvider.providerName
            Picasso.get()
                    .load(quoteProvider.providerImageUrl)
                    .fit()
                    .centerCrop()
                    .error(R.drawable.ic_globe)
                    .into(providerImage)
        }
    }
}