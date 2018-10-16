package com.thing.quoter

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import com.thing.quoter.model.Quote
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.header_section.*

class MainActivity : AppActivity(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.themeToggle -> {
                toggleTheme()
            }
            R.id.fontToggle -> {
                //TODO change font app globally
            }
        }
    }

    fun updateQuote(quote: Quote?) {
        quoteContainer.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in)).run {
            if (quote == null) return show("Finding the right quotes for you")
            quoteTextView.text = quote.quote
            speakerTextView.text = getString(R.string.speaker_format, if (quote.speaker.isEmpty()) getString(R.string.speaker_unknown) else quote.speaker)
        }
    }

    //Replacement for Toast
    fun show(message: String, speaker: String = getString(R.string.app_name)) {
        updateQuote(Quote(message, speaker))
    }

    fun onboard() {
        val onboardMsgs = resources.getStringArray(R.array.onboard_msgs)
        var i = 0
        show(onboardMsgs[i++], getString(R.string.company_name))
        rootView.setOnClickListener {
            if (i == onboardMsgs.size) {
                toggleTheme()
                isFirstTime = false
                return@setOnClickListener
            }
            show(onboardMsgs[i++], getString(R.string.company_name))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        themeToggle.setOnClickListener(this)
        if (isFirstTime)
            onboard()
        else {
            updateQuote(QuoteHelper.getQuote(true))
            //then register click
            rootView.setOnClickListener {
                updateQuote(QuoteHelper.getQuote())
            }
        }
    }
}