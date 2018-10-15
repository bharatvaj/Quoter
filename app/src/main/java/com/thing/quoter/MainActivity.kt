package com.thing.quoter

import android.os.Bundle
import android.util.Log
import android.view.View
import com.thing.quoter.model.Quote
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.header_section.*
import java.util.*

class MainActivity : AppActivity(), View.OnClickListener {


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.themeToggle -> {
                toggleTheme()
            }
        }
    }


    fun updateQuote(quote: Quote) {
        quoteTextView.text = quote.quote
        speakerTextView.text = getString(R.string.speaker_format, quote.speaker)
    }

    private lateinit var quotes: FirestoreList<Quote>
    override fun onStart() {
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        themeToggle.setOnClickListener(this)

        updateQuote(QuoteHelper.getRandomQuote())
        //then register click
        rootView.setOnClickListener {
            updateQuote(QuoteHelper.getRandomQuote())
        }
    }
}