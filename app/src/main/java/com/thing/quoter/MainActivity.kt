package com.thing.quoter

import android.databinding.ObservableArrayMap
import android.databinding.ObservableMap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.thing.quoter.model.Quote
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    fun getNewQuote(): Quote {
        quotes.populate(100)
        return if (quotes.size == 0)
            Quote("No new quotes, enjoy your life", "Quoter dev")
        else quotes.keyAt(0)
    }

    fun updateQuote(quote: Quote) {
        quoteTextView.text = quote.quote
        speakerTextView.text = quote.speaker
    }

    private lateinit var quotes: FirestoreList<Quote>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        quotes = FirestoreList(Quote::class.java, QuoteHelper.getQuotesRef())
        //register callbacks
        class onAdd : ObservableMap.OnMapChangedCallback<ObservableMap<Quote, String>, Quote, String>() {
            override fun onMapChanged(sender: ObservableMap<Quote, String>, key: Quote) {
                updateQuote(key)
            }
        } //TODO simplify
        quotes.addOnMapChangedCallback(onAdd())
        //at start
        updateQuote(getNewQuote())
        //then register click
        rootView.setOnClickListener {
            updateQuote(getNewQuote())
        }
    }
}