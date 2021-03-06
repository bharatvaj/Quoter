package com.thing.quoter.repository.model

import java.io.Serializable

data class QuoteSetting(
        var isPreview: Boolean,
        var fontFamily: String,
        var fontSize: Int
) : Serializable {
    constructor() : this(false, "serif", 38)
}