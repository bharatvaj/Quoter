package com.thing.quoter

import android.annotation.SuppressLint
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.thing.quoter.model.Quote

object QuoteHelper {
    @SuppressLint("StaticFieldLeak") //TODO CORRECT
    private var db : FirebaseFirestore
    private var quotesRef : CollectionReference
    init {
        db = FirebaseFirestore.getInstance();
        quotesRef = db.collection("quotes");
    }

    fun getQuotesRef() : CollectionReference {
        return quotesRef
    }
}