package com.thing.quoter

import android.os.Bundle
import android.transition.Fade
import android.view.*
import com.squareup.picasso.Picasso
import com.thing.quoter.fragment.CustomizeFragment
import com.thing.quoter.fragment.DisplayQuoteFragment
import com.thing.quoter.fragment.ProviderSelectFragment
import com.thing.quoter.model.Quote
import com.thing.quoter.model.QuoteProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.navigation.*
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppActivity(), View.OnClickListener,
        CustomizeFragment.OnCustomizeListener,
        ProviderSelectFragment.OnProviderChangedListener,
        View.OnLongClickListener {

    override fun onLongClick(v: View?): Boolean {
        return false
    }

    override fun onProviderChanged(providerUrl: String) {
        //get provider from QuoteHelper
    }

    override fun onCustomize(viewId: Int, extraStr: String?) {
        when (viewId) {
            R.id.colorChange -> {
                toggleTheme()
            }
            CustomizeFragment.BACKGROUND_IMAGE -> {
                if (extraStr == null) {
                    return
                }
                if (!isDark) {
                    toggleTheme()
                    return
                }
                Picasso.get().load(extraStr).into(bg)
            }
            R.id.fontSpinner -> {
                if (extraStr == null) {
                    return
                }
                //
            }
            R.id.fontSizeSpinner -> {

            }
            R.id.quoteContainer -> {

            }
        }
    }

    override fun onClick(v: View) {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            return
        }
        when (v.id) {
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
//            R.id.shareBtn -> {
//                val shareIntent: Intent = Intent().apply {
//                    action = Intent.ACTION_SEND
//                    putExtra(Intent.EXTRA_STREAM, getImageUri(getBitmapFromView(imageQuoteContainer)))
//                    type = "image/*"
//                }
//                startActivity(Intent.createChooser(shareIntent, resources.getText(R.string.share_text)))
//            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        theme.applyStyle(if (isDark) R.style.AppTheme else R.style.AppTheme_Light, true)
        setContentView(R.layout.activity_main)

        val dqf: DisplayQuoteFragment = DisplayQuoteFragment()
        var quoteSource: QuoteSource? = null

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.mainFrameLayout, dqf)
                .runOnCommit {
                    if (isFirstTime) {
                        menuContainer.visibility = View.GONE
                        var quotes = ArrayList<Quote>()
                        for (quoteMsg in resources.getStringArray(R.array.onboard_msgs)) {
                            quotes.add(Quote(quoteMsg, getString(R.string.company_name)))
                        }
                        quoteSource = QuoteSource(QuoteProvider(), quotes).apply {
                            onLoadedListener = {
                                dqf.show(it)
                            }
                        }

                        quoteSource?.populate()
                        return@runOnCommit
                    } else {
                        quoteSource = QuoteSource(QuoteProvider()).apply {
                            onLoadedListener = {
                                dqf.show(it)
                            }
                        }
                        //then register click
                        providerSelect.setOnClickListener(this)
                        customizeSelect.setOnClickListener(this)
                        shareBtn.setOnClickListener(this)

                        quoteSource?.populate()
                    }
                    leftNavBtn.setOnClickListener {
                        var quote = quoteSource?.getPreviousQuote()
                        if (quoteSource == null) {
                            if (isFirstTime) {
                                isFirstTime = false
                                recreate()
                            }
                        } else dqf.show(quote)
                    }
                    rightNavBtn.setOnClickListener {
                        var quote = quoteSource?.getNextQuote()
                        if (quoteSource == null) {
                            //reached end
                        } else dqf.show(quote)
                    }
                }
                .commit()
    }
}