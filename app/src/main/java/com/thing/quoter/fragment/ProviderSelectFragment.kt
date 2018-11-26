package com.thing.quoter.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thing.quoter.helper.QuoterHelper

import com.thing.quoter.R
import com.thing.quoter.adapter.ProviderViewAdapter
import kotlinx.android.synthetic.main.fragment_provider_select.*

class ProviderSelectFragment : Fragment() {
    private var listener: OnProviderChangedListener? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_provider_select, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ProviderViewAdapter(context!!, QuoterHelper.quoteProviders)
        providerRecyclerView.adapter = adapter
        providerRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnProviderChangedListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnProviderChangedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnProviderChangedListener {
        fun onProviderChanged(providerUrl: String)
    }
}
