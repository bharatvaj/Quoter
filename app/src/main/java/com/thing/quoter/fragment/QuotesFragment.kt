package com.thing.quoter.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.thing.quoter.helper.QuoteSource

import com.thing.quoter.R


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
