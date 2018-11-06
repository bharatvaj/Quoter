package com.thing.quoter

import com.thing.quoter.model.Quote
import java.util.Random
import kotlin.collections.ArrayList

object QuoterHelper {
    private var quotes = ArrayList<Quote>()
    private var randomIndex = Random()
    var imagesUrl = "https://source.unsplash.com/random"
    var shouldLoadImage = false
    var stashedQuote: Quote? = null
    private var i = 0

    init {
        quotes.add(Quote("New Quote should be added from goodreads", "Quoter"))
    }

    fun getQuote(): Quote? {
        stashedQuote = if (quotes.isEmpty()) null else quotes[i++ % quotes.size]
        return stashedQuote
    }
}