package com.thing.quoter.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.thing.quoter.helper.QuoterHelper

import com.thing.quoter.R
import com.thing.quoter.adapter.ProviderViewAdapter
import com.thing.quoter.databinding.FragmentProviderSelectBinding

class ProviderSelectFragment : Fragment() {
    private var listener: OnProviderChangedListener? = null
    private lateinit var binding: FragmentProviderSelectBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentProviderSelectBinding.inflate(inflater, container, false)
        return binding.root
//        return inflater.inflate(R.layout.fragment_provider_select, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ProviderViewAdapter(context!!, QuoterHelper.quoteProviders)
        binding.providerRecyclerView.adapter = adapter
        binding.providerRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
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
