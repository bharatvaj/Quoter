package com.thing.quoter.model

class Quote(val quote: String, val author: String, val tags: List<String>? = null) {
    constructor() : this("", "")
}