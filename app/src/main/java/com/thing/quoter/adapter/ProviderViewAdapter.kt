package com.thing.quoter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.thing.quoter.databinding.ProviderItemBinding
import com.thing.quoter.repository.model.QuoteProvider

class ProviderViewAdapter(val context: Context, private val providers: Array<QuoteProvider>) : RecyclerView.Adapter<ProviderViewAdapter.ProviderViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ProviderViewHolder {
        val binding = ProviderItemBinding.inflate(LayoutInflater.from(context), p0, false)
        return ProviderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return providers.size
    }

    override fun onBindViewHolder(p0: ProviderViewHolder, p1: Int) {
        p0.setIsRecyclable(false)
        val quoteProvider = providers[p1]
        p0.bind(quoteProvider)
    }

    class ProviderViewHolder(val itemBinding: ProviderItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        var providerImage: ImageView = itemBinding.providerImage
        val providerName: TextView = itemBinding.providerName
        fun bind(quoteProvider: QuoteProvider) {
            providerName.text = quoteProvider.providerName
//            Picasso.get()
//                    .load(R.drawable.ic_globe)
//                    .into(providerImage)
        }
    }
}