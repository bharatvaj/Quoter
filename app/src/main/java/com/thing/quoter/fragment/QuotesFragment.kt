package com.thing.quoter.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thing.quoter.QuoteSource

import com.thing.quoter.R
import kotlinx.android.synthetic.main.navigation.*


class QuotesFragment : Fragment() {

    private var listener: OnQuoteChangedListener? = null

    fun setOnQuoteChangedListener(listener: OnQuoteChangedListener) {
        this.listener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quotes, container, false)
    }

//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        if (context is OnQuoteChangedListener) {
//            listener = context
//        } else {
//            throw RuntimeException(context.toString() + " must implement OnQuoteChangedListener")
//        }
//    }
//
//    override fun onDetach() {
//        super.onDetach()
//        listener = null
//    }

    interface OnQuoteChangedListener {
        fun onQuoteChanged(quoteSource: QuoteSource, position: Int)
    }

}
