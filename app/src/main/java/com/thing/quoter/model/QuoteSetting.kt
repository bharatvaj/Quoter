package com.thing.quoter.model

import java.io.Serializable

data class QuoteSetting(
        var isPreview: Boolean,
        var quoteFontFamily: String,
        var quoteFontSize: Float,
        var authorFontFamily: String,
        var authorFontSize: Float,
        var bg: String
) : Serializable {
    constructor() : this(false, "serif",
            38f, "", 22f, "black")
}