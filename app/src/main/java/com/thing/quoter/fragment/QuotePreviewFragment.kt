package com.thing.quoter.fragment

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.interpolator.view.animation.FastOutSlowInInterpolator

import com.thing.quoter.R
import com.thing.quoter.repository.model.Quote
import com.thing.quoter.repository.model.QuoteSetting
import kotlinx.android.synthetic.main.fragment_quote_preview.*


class QuotePreviewFragment : Fragment(), View.OnLongClickListener {
    private var quoteSetting: QuoteSetting = QuoteSetting()
    private var listener: View.OnLongClickListener? = null

    override fun onLongClick(v: View): Boolean {
        if (listener == null) return false
        return listener!!.onLongClick(v)
    }

    fun show(message: String, speaker: String = getString(R.string.app_name)) {
        show(Quote(message, speaker))
    }

    fun show(quote: Quote?) {
        if (quote == null) return show(getString(R.string.quote_loading))
        quoteContainer.startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_in).apply {
            interpolator = FastOutSlowInInterpolator()
        })
        quoteTextView.text = if (quote.quote.isEmpty()) context?.getString(R.string.quote_loading) else quote.quote
        authorTextView.text = if (quote.author.isEmpty()) context?.getString(R.string.author_unknown) else quote.author
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_quote_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        show(null)
//        authorTextView.text = if (quote!!.author.isEmpty()) context!!.getString(R.string.author_unknown) else quote!!.author //FIXME HAVE TO IMPLEMENT THIS LATER
//        quoteTextView.setOnLongClickListener(this)
    }

    fun loadSetting(quoteSetting: QuoteSetting) {
        quoteTextView.typeface = if (quoteSetting.fontFamily == "serif") Typeface.SERIF
        else if (quoteSetting.fontFamily == "monospace") Typeface.MONOSPACE else Typeface.SANS_SERIF //FIXME Too much information hard coded
        quoteTextView.textSize = quoteSetting.fontSize.toFloat()
    }

    fun getSetting(): QuoteSetting {
        return quoteSetting
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is View.OnLongClickListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnLongClickListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
