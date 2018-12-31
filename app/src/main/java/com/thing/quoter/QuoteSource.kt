package com.thing.quoter

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.thing.quoter.model.Quote
import com.thing.quoter.model.QuoteProvider

class QuoteSource(var quoteProvider: QuoteProvider, var extra: ArrayList<Quote>? = null) {

    //FIXME Use actual database
    private var quotesRef: CollectionReference = FirebaseFirestore.getInstance().collection("quotes")
    var firestoreQuotes = FirestoreList(Quote::class.java, quotesRef)
    ///////////////////////////////////////////
    var quotes = ArrayList<Quote>()
    var index: Int = 0
    var onLoadedListener: ((Quote) -> Unit)? = null

    fun populate() {
        if (extra != null) {
            for (quote in extra!!) {
                quotes.add(quote)
            }
            onLoadedListener?.invoke(extra!!.first())
            index++
            return
        }
        var isFirst = true
        //FIXME Read from JSONQuoteMapper class
        firestoreQuotes.setOnAddListener { _, quote ->
            if (isFirst) {
                onLoadedListener?.invoke(quote)
                index++
            }
            quotes.add(quote)
            isFirst = false
        }
    }

    fun getNextQuote(): Quote? {
        return if (quotes.size == 0 || index == quotes.size) null
        else quotes[index++]
    }

    fun getPreviousQuote(): Quote? {
        return if (quotes.size == 0 || index == -1) null
        else quotes[index--]
    }
}