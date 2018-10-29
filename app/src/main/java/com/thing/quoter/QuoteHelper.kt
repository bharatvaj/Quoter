package com.thing.quoter

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.thing.quoter.model.Quote
import java.util.Random

object QuoteHelper {
    private var quotesRef: CollectionReference = FirebaseFirestore.getInstance().collection("quotes")
    private var quotes = FirestoreList(Quote::class.java, quotesRef)
    private var randomIndex = Random()
    var imagesUrl = "https://source.unsplash.com/random?w=320,h=200"
    var shouldLoadImage = false
    var stashedQuote: Quote? = null
    private var i = 0

    fun getQuote(): Quote? {
        stashedQuote = if (quotes.size == 0) null else quotes.keyAt(i++ % quotes.size)
        return stashedQuote
    }
}