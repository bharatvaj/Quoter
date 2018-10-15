package com.thing.quoter

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.thing.quoter.model.Quote
import java.util.*

object QuoteHelper {
    private var quotesRef: CollectionReference = FirebaseFirestore.getInstance().collection("quotes")
    private var quotes = FirestoreList(Quote::class.java, quotesRef)
    private var randomIndex = Random()

    fun getRandomQuote(): Quote {
        return if (quotes.size == 0)
            Quote("Loading awesome quotes...", "Quoter")
        else quotes.keyAt(randomIndex.nextInt(quotes.size))
    }

}