package com.thing.quoter

import android.content.Intent
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

    val dqf: DisplayQuoteFragment = DisplayQuoteFragment()
    var quoteSource: QuoteSource? = null

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
                if (dqf.isVisible) {
                    dqf.loadSetting(dqf.getSetting().apply { fontFamily = extraStr })
                }
            }
            R.id.fontSizeSpinner -> {
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
            R.id.shareBtn -> {
                showMenu(false)
                val shareIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM, getImageUri(getBitmapFromView(rootView)))
                    type = "image/*"
                }
                showMenu(true)
                startActivity(Intent.createChooser(shareIntent, resources.getText(R.string.share_text)))
            }
        }
    }

    fun showMenu(b: Boolean) {
        val v = if (b) View.VISIBLE else View.GONE
        menuContainer.visibility = v
        navBar.visibility = v
    }

    fun navLeft() {
        val quote = quoteSource?.getPreviousQuote()
        if (quote == null)
            return
        dqf.show(quote)
        rightNavBtn.visibility = View.VISIBLE
    }

    fun navRight() {
        val quote = quoteSource?.getNextQuote()
        if (quote == null)
            return
        dqf.show(quote)
        leftNavBtn.visibility = View.VISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isFirstTime) {
            theme.applyStyle(R.style.AppTheme_Light, true)
        }
        setContentView(R.layout.activity_main)

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.mainFrameLayout, dqf)
                .runOnCommit {
                    if (isFirstTime) {
                        menuContainer.visibility = View.GONE
                        val quotes = ArrayList<Quote>()
                        for (quoteMsg in resources.getStringArray(R.array.onboard_msgs)) {
                            quotes.add(Quote(quoteMsg, getString(R.string.company_name)))
                        }
                        quoteSource = QuoteSource(QuoteProvider(), quotes).apply {
                            onEndReachedListener = {
                                isFirstTime = false
                                recreate()
                            }
                        }
                    } else {
                        quoteSource = QuoteSource(QuoteProvider())
                        //then register click
                        providerSelect.setOnClickListener(this)
                        customizeSelect.setOnClickListener(this)
                        shareBtn.setOnClickListener(this)
                        mainFrameLayout.setOnClickListener(this)

                        quoteSource?.onEndReachedListener = {
                            dqf.show(it)
                            rightNavBtn.visibility = View.GONE
                            leftNavBtn.visibility = View.VISIBLE
                        }
                    }
                    //assigning callbacks
                    leftNavBtn.setOnClickListener {
                        navLeft()
                    }
                    rightNavBtn.setOnClickListener {
                        navRight()
                    }
                    quoteSource?.onStartReachedListener = {
                        dqf.show(it)
                        leftNavBtn.visibility = View.GONE
                        rightNavBtn.visibility = View.VISIBLE
                    }
                    //populate
                    quoteSource?.populate()
                }
                .commit()
        mainFrameLayout.setOnTouchListener(object : OnSwipeTouchListener(this) {
            override fun onClick() {
                supportFragmentManager.popBackStack()
            }

            override fun onSwipeLeft() {
                this@MainActivity.navRight()
            }

            override fun onSwipeRight() {
                this@MainActivity.navLeft()
            }
        })
    }
}