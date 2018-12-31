package com.thing.quoter.model

import java.io.Serializable

data class QuoteSetting(
        var isPreview: Boolean,
        var fontFamily: String
) : Serializable {
    constructor() : this(false, "serif")
}