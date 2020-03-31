package com.thing.quoter.helper

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.thing.quoter.repository.model.Quote
import com.thing.quoter.repository.model.QuoteProvider

class QuoteSource(var quoteProvider: QuoteProvider, var extra: ArrayList<Quote>? = null) {

    //FIXME Use actual database
    private var quotesRef: CollectionReference = FirebaseFirestore.getInstance().collection("quotes")
    var firestoreQuotes =
        FirestoreList(Quote::class.java, quotesRef)
    ///////////////////////////////////////////
    var quotes = ArrayList<Quote>()
    var index: Int = -1
    var onStartReachedListener: ((Quote) -> Unit)? = null
    var onEndReachedListener: ((Quote) -> Unit)? = null

    fun populate() {
        if (extra != null) {
            for (quote in extra!!) {
                quotes.add(quote)
            }
            onStartReachedListener?.invoke(extra!!.first())
            index++
            return
        }
        var isFirst = true

        //FIXME Read from JSONQuoteMapper class
        firestoreQuotes.setOnAddListener { _, quote ->
            if (isFirst) {
                onStartReachedListener?.invoke(quote)
                index++
            }
            quotes.add(quote)
            isFirst = false
        }
        //////////////////////////////////////////////////////////////
    }

    fun getNextQuote(): Quote? {
        if (quotes.size == 0) return null
        if (index + 1> quotes.size - 1) return null
        index++
        if (quotes.size - 1 == index) onEndReachedListener?.invoke(quotes[quotes.size - 1])
        return quotes[index]
    }

    fun getPreviousQuote(): Quote? {
        if (quotes.size == 0) return null
        if (index - 1 < 0) return null
        index--
        if (index == 0) onStartReachedListener?.invoke(quotes[0])
        return quotes[index]
    }
}