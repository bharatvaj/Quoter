package com.thing.quoter.model

class Quote(val quote: String, val speaker: String, val tags: List<String>? = null) {
    constructor() : this("", "")
}