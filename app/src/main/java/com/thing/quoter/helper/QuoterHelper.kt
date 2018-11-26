package com.thing.quoter.helper

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.thing.quoter.FirestoreList
import com.thing.quoter.model.Quote
import com.thing.quoter.model.QuoteProvider
import java.util.*
import kotlin.collections.ArrayList

object QuoterHelper {
    private var quotesRef: CollectionReference = FirebaseFirestore.getInstance().collection("quotes")
    private var quotes = FirestoreList(Quote::class.java, quotesRef)
    private var randomIndex = Random()

    private const val TAG = "QuoterHelper"

    var quoteProviders = ArrayList<QuoteProvider>()
    private var quoteList = ArrayList<Quote>()

    var imagesUrl = "https://source.unsplash.com/random/800x600"
    var shouldLoadImage = false
    var stashedQuote: Quote? = null
    private var i = 0

    init {
        quoteProviders.add(QuoteProvider("Internet", "", "", "", "", "", "", ""))
    }


    fun getQuote(): Quote? {
        stashedQuote = if (quotes.isEmpty) null else quotes.keyAt(i++ % quotes.size)
        return stashedQuote
    }

    fun getBackgroundUrl() : String{

    }
}