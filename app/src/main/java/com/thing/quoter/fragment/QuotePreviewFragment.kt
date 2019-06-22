package com.thing.quoter.fragment

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.squareup.picasso.Picasso

import com.thing.quoter.R
import com.thing.quoter.model.Quote
import com.thing.quoter.model.QuoteSetting
import kotlinx.android.synthetic.main.fragment_quote_preview.*


class QuotePreviewFragment : Fragment(), View.OnLongClickListener {

    companion object {
        const val ARG_1 = "QUOTE_SETTING"
    }

    private var quoteSetting: QuoteSetting? = null
    private var listener: View.OnLongClickListener? = null

    fun newInstance(quoteSetting: QuoteSetting): QuotePreviewFragment {
        return QuotePreviewFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_1, quoteSetting)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            quoteSetting = arguments?.getSerializable(ARG_1) as QuoteSetting
        }
    }

    override fun onLongClick(v: View): Boolean {
        if (listener == null) return false
        return listener!!.onLongClick(v)
    }

    fun show(message: String, speaker: String = getString(R.string.app_name)) {
        show(Quote(message, speaker))
    }

    fun show(quote: Quote?) {
        if (quote == null) return show(getString(R.string.quote_loading))
        quoteTextView.startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_in).apply {
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

    fun setQuoteTextFont(font: String) {
        quoteTextView.typeface = if (font == "serif") Typeface.SERIF
        else if (font == "monospace") Typeface.MONOSPACE else Typeface.SANS_SERIF //FIXME Too much information hard coded
        quoteSetting?.quoteFontFamily = font
    }

    fun setQuoteTextSize(fontSize: Float) {
        quoteTextView.textSize = fontSize
        quoteSetting?.quoteFontSize = fontSize;
    }

    fun setQuoteBackground(bg: String) {
        Picasso.get().load(bg).into(backgroundImageView)
        quoteSetting?.bg = bg
    }

    fun getSetting(): QuoteSetting {
        return quoteSetting!!
    }

    fun setSetting(quoteSetting: QuoteSetting) {
        setQuoteBackground(quoteSetting.bg)
        setQuoteTextFont(quoteSetting.quoteFontFamily)
        setQuoteTextSize(quoteSetting.quoteFontSize)
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
