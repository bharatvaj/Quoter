package com.thing.quoter.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.thing.quoter.helper.QuoterHelper

import com.thing.quoter.R
import com.thing.quoter.adapter.BackgroundPreviewViewAdapter
import com.thing.quoter.repository.model.QuoteSetting
import kotlinx.android.synthetic.main.customize_background.*
import kotlinx.android.synthetic.main.customize_text.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CustomizeFragment : Fragment(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    var qpf: QuotePreviewFragment? = null

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id!!) {
            R.id.fontSpinner -> {
                val extraStr = context!!.resources.getStringArray(R.array.font_list)[position]
                qpf!!.loadSetting(QuoterHelper.quoteSetting!!.apply { fontFamily = extraStr })
                textListener?.onTextCustomize(QuoterHelper.quoteSetting!!)
            }
            R.id.fontSizeSpinner -> {
                val extraStr = context!!.resources.getStringArray(R.array.font_size_list)[position]
                qpf!!.loadSetting(QuoterHelper.quoteSetting!!.also { it.fontSize = extraStr.toInt() })
                textListener?.onTextCustomize(QuoterHelper.quoteSetting!!)
            }
        }
    }

    companion object {
        const val BACKGROUND_IMAGE = 1
    }

    var adapter: BackgroundPreviewViewAdapter? = null

    private var listener: OnCustomizeListener? = null
    private var textListener: OnTextCustomizeListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
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

        qpf = QuotePreviewFragment()
        fragmentManager?.beginTransaction()
                ?.replace(R.id.textPreviewFrameLayout, qpf!!)
                ?.runOnCommit {
                    qpf!!.show(getString(R.string.preview_string))
                }?.commit()

        ArrayAdapter.createFromResource(
                context!!,
                R.array.font_list,
                android.R.layout.simple_spinner_item
        ).also { a ->
            a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            fontSpinner.adapter = a
        }

        ArrayAdapter.createFromResource(
                context!!,
                R.array.font_size_list,
                android.R.layout.simple_spinner_item
        ).also { a ->
            a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            fontSizeSpinner.adapter = a
        }
        fontSpinner.setSelection(context!!.resources.getStringArray(R.array.font_list).indexOf(
            QuoterHelper.quoteSetting!!.fontFamily))
        fontSizeSpinner.setSelection(context!!.resources.getStringArray(R.array.font_size_list).indexOf(
            QuoterHelper.quoteSetting!!.fontSize.toString()))

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
        } else
            throw RuntimeException(context.toString() + " must implement OnProviderChangedListener")
        if (context is OnTextCustomizeListener) {
            textListener = context
        } else throw java.lang.RuntimeException(context.toString() + "must implement OnTextCustomizeListener")
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
        textListener = null
    }

    interface OnCustomizeListener {
        fun onCustomize(viewId: Int, extraStr: String? = null)
    }

    interface OnTextCustomizeListener {
        fun onTextCustomize(quoteSetting: QuoteSetting)
    }
}
