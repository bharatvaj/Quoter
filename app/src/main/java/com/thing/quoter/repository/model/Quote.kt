package com.thing.quoter.repository.model

import java.io.Serializable

data class Quote(val quote: String, val author: String, val tags: List<String>? = null) : Serializable {
    constructor() : this("", "")
}