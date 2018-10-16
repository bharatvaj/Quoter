package com.thing.quoter

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.thing.quoter.model.Quote
import java.util.*

object QuoteHelper {
    private var quotesRef: CollectionReference = FirebaseFirestore.getInstance().collection("quotes")
    private var quotes = FirestoreList(Quote::class.java, quotesRef)
    private var randomIndex = Random()
    private var cachedQuote: Quote? = null
    private var i = 0

    fun getQuote(requestLast: Boolean = false): Quote? {
        if (requestLast) {
            return cachedQuote
        }
        cachedQuote = if (quotes.size == 0) null else quotes.keyAt(i++ % quotes.size)
        return cachedQuote
    }
}