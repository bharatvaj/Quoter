package com.thing.quoter

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.transition.Fade
import android.view.*
import android.view.animation.AnimationUtils
import com.squareup.picasso.Picasso
import com.thing.quoter.fragment.CustomizeFragment
import com.thing.quoter.fragment.ProviderSelectFragment
import com.thing.quoter.model.Quote
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.footer_section.*
import kotlinx.android.synthetic.main.header_section.*


class MainActivity : AppActivity(), View.OnClickListener, CustomizeFragment.OnCustomizeListener, ProviderSelectFragment.OnProviderChangedListener {
    override fun onProviderChanged(providerUrl: String) {
        //get provider from QuoteHelper
    }

    override fun onCustomize(viewId: Int, extraStr: String?) {
        when (viewId) {
            R.id.colorChange -> {
                toggleTheme()
            }
            R.id.toggleFont -> {
                toggleFont()
            }
            CustomizeFragment.BACKGROUND_IMAGE -> {
                if (extraStr == null) {
                    return
                }
                if (!isDark) {
                    QuoterHelper.shouldLoadImage = true
                    QuoterHelper.stashedImage = extraStr
                    toggleTheme()
                    return
                }
                Picasso.get().load(extraStr).into(bg)
                return
            }

            R.id.toggleSpeaker -> {
                authorTextView.visibility = if (authorTextView.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
        }
    }

    override fun onClick(v: View) {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            return
        }
        when (v.id) {
            R.id.imageQuoteContainer -> {
                show(QuoterHelper.getQuote())
            }
            R.id.customizeSelect -> {
                supportFragmentManager.beginTransaction()
                        .addToBackStack("customize")
                        .replace(R.id.optionFrameLayout, CustomizeFragment().apply {
                            enterTransition = Fade(Fade.MODE_IN)
                            exitTransition = Fade(Fade.MODE_OUT)
                        })
                        .commit()
            }
            R.id.providerSelect -> {
                supportFragmentManager.beginTransaction()
                        .addToBackStack("provider")
                        .replace(R.id.optionFrameLayout, ProviderSelectFragment().apply {
                            enterTransition = Fade(Fade.MODE_IN)
                            exitTransition = Fade(Fade.MODE_OUT)
                        })
                        .commit()
            }
            R.id.shareBtn -> {
                val shareIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM, getImageUri(getBitmapFromView(imageQuoteContainer)))
                    type = "image/*"
                }
                startActivity(Intent.createChooser(shareIntent, resources.getText(R.string.share_text)))
            }
        }
    }

    override fun show(quote: Quote?) {
        if (quote == null) return show(resources.getString(R.string.quote_loading))
        quoteContainer.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in).apply {
            interpolator = FastOutSlowInInterpolator()
        })
        quoteTextView.text = quote.quote
        authorTextView.text = if (quote.author.isEmpty()) getString(R.string.speaker_unknown) else quote.author
    }


    fun onboard() {
        val onboardMsgs = resources.getStringArray(R.array.onboard_msgs)
        var i = 0
        show(onboardMsgs[i++], getString(R.string.company_name))
        imageQuoteContainer.setOnClickListener {
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
        quoteTextView.typeface = if (isFontSerif) Typeface.SERIF else Typeface.DEFAULT
        //

        if (isFirstTime) {
            menuContainer.visibility = View.GONE
            return onboard()
        } else {
            show(QuoterHelper.stashedQuote)
            //then register click
            imageQuoteContainer.setOnClickListener(this)
            providerSelect.setOnClickListener(this)
            customizeSelect.setOnClickListener(this)
            shareBtn.setOnClickListener(this)
        }
        if (QuoterHelper.shouldLoadImage) {
            onCustomize(CustomizeFragment.BACKGROUND_IMAGE, QuoterHelper.stashedImage)
            QuoterHelper.shouldLoadImage = false
        }
    }
}