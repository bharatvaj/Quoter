package com.thing.quoter

import android.content.Context
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.thing.quoter.model.Quote
import java.util.*

object QuoteHelper {
    private var quotesRef: CollectionReference = FirebaseFirestore.getInstance().collection("quotes")
    private var quotes = FirestoreList(Quote::class.java, quotesRef)
    private var randomIndex = Random()
    private var imagesUrl = "https://source.unsplash.com/random?w=320,h=200"
    private var cachedQuote: Quote? = null
    private var cachedImage: String = ""
    private var i = 0

    fun getQuote(requestLast: Boolean = false): Quote? {
        if (requestLast) {
            return cachedQuote
        }
        cachedQuote = if (quotes.size == 0) null else quotes.keyAt(i++ % quotes.size)
        return cachedQuote
    }

    fun getImage(requestLast: Boolean = false): String {
        if(requestLast){
            return cachedImage
        }
        cachedImage = imagesUrl
        return cachedImage
    }
}