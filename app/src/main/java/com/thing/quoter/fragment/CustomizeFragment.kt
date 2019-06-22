package com.thing.quoter.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.thing.quoter.QuoterHelper

import com.thing.quoter.R
import com.thing.quoter.adapter.BackgroundPreviewViewAdapter
import com.thing.quoter.model.QuoteSetting
import kotlinx.android.synthetic.main.customize_background.*
import kotlinx.android.synthetic.main.customize_text.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CustomizeFragment : Fragment(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    companion object {
        const val BACKGROUND_IMAGE = 1
        const val ARG_1 = "QUOTE_SETTING"

        var quoteSetting: QuoteSetting? = null

        fun newInstance(quoteSetting: QuoteSetting): CustomizeFragment {
            return CustomizeFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_1, quoteSetting)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            quoteSetting = arguments?.getSerializable(ARG_1) as QuoteSetting
        }
    }

    var qpf: QuotePreviewFragment? = null

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id!!) {
            R.id.fontSpinner -> {
                val extraStr = context!!.resources.getStringArray(R.array.font_list)[position]
                qpf?.setQuoteTextFont(extraStr)
                listener?.onCustomize(R.id.fontSpinner, extraStr)
            }
        }
    }

    var adapter: BackgroundPreviewViewAdapter? = null

    private var listener: OnCustomizeListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_customize, container, false)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun notifyBackgroundList(backgroundUpdateEvent: QuoterHelper.BackgroundUpdateEvent) {
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
        toggleSpeaker.setOnClickListener(this)

        adapter = BackgroundPreviewViewAdapter(context!!, QuoterHelper.backgrounds)
        backgroundPreviewRecyclerView.adapter = adapter
        backgroundPreviewRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter?.listener = fun(str: String) {
            listener?.onCustomize(BACKGROUND_IMAGE, str)
        }

        qpf = QuotePreviewFragment().newInstance(quoteSetting!!)
        fragmentManager?.beginTransaction()
                ?.replace(R.id.textPreviewFrameLayout, qpf!!)
                ?.runOnCommit {
                    qpf!!.show(getString(R.string.preview_string))
                    ArrayAdapter.createFromResource(
                            context!!,
                            R.array.font_list,
                            android.R.layout.simple_spinner_item
                    ).also { a ->
                        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        fontSpinner.adapter = a
                    }
                    fontSpinner.setSelection(context!!.resources.getStringArray(R.array.font_list).indexOf(qpf?.getSetting()!!.quoteFontFamily))
                    fontSizeEditText.setText(qpf!!.getSetting().quoteFontSize.toString())
                    fontSpinner.onItemSelectedListener = this
                    fontSizeEditText //FIXME TODO ADD UPDATION CODE HERE //
//                    val extraStr = fontSizeEditText.text.toString()
//                    qpf?.setQuoteTextSize(extraStr.toFloat())
//                    listener?.onCustomize(R.id.fontSizeEditText, extraStr)
                }?.commit()
    }

    override fun onClick(view: View) {
        listener?.onCustomize(view.id)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCustomizeListener) {
            listener = context
        } else
            throw RuntimeException(context.toString() + " must implement OnProviderChangedListener")
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnCustomizeListener {
        fun onCustomize(viewId: Int, extraStr: String? = null)
    }
}
