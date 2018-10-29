package com.thing.quoter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.signature.ObjectKey
import com.gunhansancar.changelanguageexample.helper.LocaleHelper
import com.thing.quoter.model.Quote
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.header_section.*

class MainActivity : AppActivity(), View.OnClickListener, View.OnLongClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.themeToggle -> {
                toggleTheme()
            }
            R.id.fontToggle -> {
                //TODO change font app globally
                toggleFont()
            }
            R.id.languageSelect -> {
                LocaleHelper.setLocale(this, "ta")
                recreate()
            }
            R.id.toggleSpeaker -> {
                speakerTextView.visibility = if (speakerTextView.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
        }
    }

    fun startLoader(quote: Quote? = null) {
        if (quote != null) {
            quoteTextView.text = quote.quote;
            speakerTextView.text = quote.speaker
        }
        var anim = AnimationUtils.loadAnimation(this, android.R.anim.fade_out)
        anim.repeatCount = Animation.INFINITE
        anim.repeatMode = Animation.REVERSE
        quoteContainer.startAnimation(anim)
    }

    fun stopLoader() {
        quoteContainer.clearAnimation()
    }

    override fun onLongClick(v: View?): Boolean {
        if (!isDark) {
            QuoteHelper.shouldLoadImage = true
            toggleTheme()
            return false
        }
        loadImage()
        return true
    }

    private fun loadImage() {
        startLoader()
        val requestOption = RequestOptions().centerCrop()
                .signature(ObjectKey(System.currentTimeMillis().toString()))
                .format(DecodeFormat.PREFER_RGB_565)
        Glide.with(this)
                .load(QuoteHelper.imagesUrl)
                .apply(requestOption)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(object : CustomViewTarget<RelativeLayout, Drawable>(rootView) {
                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        stopLoader()
                    }

                    @SuppressLint("ResourceType")
                    override fun onResourceCleared(placeholder: Drawable?) {
                        rootView.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.colorPrimary))
                    }

                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        stopLoader()
                        rootView.background = resource
                        rootView.background.alpha = 220
                    }
                })
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    fun updateQuote(quote: Quote?) {
        if (quote == null) return show(resources.getString(R.string.quote_loading))
        quoteTextView.text = quote.quote
        speakerTextView.text = if (quote.speaker.isEmpty()) getString(R.string.speaker_unknown) else quote.speaker
    }

    //Replacement for Toast
    fun show(message: String, speaker: String = getString(R.string.app_name)) {
        quoteContainer.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in))
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
        theme.applyStyle(if (isDark) R.style.AppTheme else R.style.AppTheme_Light, true)
        setContentView(R.layout.activity_main)
        //
        quoteTextView.typeface = if (isFontSerif) Typeface.SERIF else Typeface.SANS_SERIF
        //
        themeToggle.setOnClickListener(this)
        fontToggle.setOnClickListener(this)
        languageSelect.setOnClickListener(this)
        toggleSpeaker.setOnClickListener(this)
        rootView.setOnLongClickListener(this)
        if (isFirstTime)
            onboard()
        else {
            updateQuote(QuoteHelper.stashedQuote)
            //then register click
            rootView.setOnClickListener {
                updateQuote(QuoteHelper.getQuote())
            }
        }
        if (QuoteHelper.shouldLoadImage) {
            loadImage()
            QuoteHelper.shouldLoadImage = false
        }
    }
}