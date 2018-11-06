package com.thing.quoter.model

class QuoteProvider(val providerName: String,
                    val providerImageUrl: String,
                    val apiGateway: String,
                    val quoteAttribute: String,
                    val speakerAttribute: String,
                    val categoryAttribute: String,
                    val comment: String,
                    val providerUrl: String) {
    constructor() : this("", "", "", "", "", "Other", "", "")
}