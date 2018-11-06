package com.thing.quoter

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.transition.Fade
import android.transition.Slide
import android.view.*
import android.view.animation.AnimationUtils
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
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

    override fun onCustomize(viewId: Int) {
        when (viewId) {
            R.id.themeToggle -> {
                toggleTheme()
            }
//            R.id.fontToggle -> {
//                //TODO change font app globally
//                toggleFont()
//            }

            R.id.toggleSpeaker -> {
                authorTextView.visibility = if (authorTextView.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
            R.id.backgroundChange -> {
                if (!isDark) {
                    QuoterHelper.shouldLoadImage = true
                    toggleTheme()
                }
                loadImage()
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


    fun shouldLoad(load: Boolean) {
        progressBar.isIndeterminate = load
        progressBar.visibility = if (load) View.VISIBLE else View.INVISIBLE
    }


    private fun loadImage() {
        shouldLoad(true)
        Picasso.get()
                .load(QuoterHelper.imagesUrl)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .noPlaceholder()
                .into(object : Target {
                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
                    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                        shouldLoad(false)
                    }

                    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                        shouldLoad(false)
                        bg.setImageBitmap(bitmap)
                        bg.imageAlpha = 180
                    }
                })
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
        quoteTextView.typeface = if (isFontSerif) Typeface.SERIF else Typeface.SANS_SERIF
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
            loadImage()
            QuoterHelper.shouldLoadImage = false
        }
    }
}