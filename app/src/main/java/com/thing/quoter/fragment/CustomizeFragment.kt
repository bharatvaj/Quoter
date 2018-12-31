package com.thing.quoter.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.transition.Fade
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import com.thing.quoter.QuoterHelper

import com.thing.quoter.R
import com.thing.quoter.adapter.BackgroundPreviewViewAdapter
import com.thing.quoter.model.Quote
import kotlinx.android.synthetic.main.customize_background.*
import kotlinx.android.synthetic.main.customize_text.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CustomizeFragment : Fragment(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        listener?.onCustomize(R.id.fontSpinner, context!!.resources.getStringArray(R.array.font_list)[position])
    }

    companion object {
        const val BACKGROUND_IMAGE = 1
    }

    var adapter: BackgroundPreviewViewAdapter? = null

    private var listener: OnCustomizeListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customize, container, false)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun notifyBackgroundList(messageEvent: QuoterHelper.MessageEvent) {
        adapter?.notifyItemInserted(adapter?.itemCount!!)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        colorChange.setOnClickListener(this)
        backgroundChange.setOnClickListener(this)
        userBgChange.setOnClickListener(this)

//        toggleFont.setOnClickListener(this)
        toggleSpeaker.setOnClickListener(this)

        adapter = BackgroundPreviewViewAdapter(context!!, QuoterHelper.backgrounds)
        backgroundPreviewRecyclerView.adapter = adapter
        backgroundPreviewRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter?.listener = fun(str: String) {
            listener?.onCustomize(BACKGROUND_IMAGE, str)
        }

        var dqf = DisplayQuoteFragment()
        fragmentManager?.beginTransaction()
                ?.replace(R.id.textPreviewFrameLayout, dqf)
                ?.runOnCommit {
                    dqf.show(getString(R.string.preview_string))
                }?.commit()



        ArrayAdapter.createFromResource(
                context,
                R.array.font_list,
                android.R.layout.simple_spinner_item
        ).also { a ->
            // Specify the layout to use when the list of choices appears
            a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            fontSpinner.adapter = a
        }
        fontSpinner.onItemSelectedListener = this
        fontSizeSpinner.onItemSelectedListener = this
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
        fun onCustomize(viewId: Int, extraStr: String? = null)
    }
}
