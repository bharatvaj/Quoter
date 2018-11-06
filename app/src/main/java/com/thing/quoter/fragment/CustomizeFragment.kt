package com.thing.quoter.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.thing.quoter.R
import kotlinx.android.synthetic.main.fragment_customize.*


class CustomizeFragment : Fragment(), View.OnClickListener {

    private var listener: OnCustomizeListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customize, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        themeToggle.setOnClickListener(this)
        //fontToggle.setOnClickListener(this)
        toggleSpeaker.setOnClickListener(this)
        backgroundChange.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        listener?.onCustomize(view.id)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCustomizeListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnProviderChangedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnCustomizeListener {
        fun onCustomize(viewId: Int)
    }
}
