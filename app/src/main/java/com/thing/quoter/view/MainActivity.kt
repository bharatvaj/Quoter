package com.thing.quoter.view

import android.content.Intent
import android.os.Bundle
import android.transition.Fade
import android.view.*
import com.squareup.picasso.Picasso
import com.thing.quoter.AppActivity
import com.thing.quoter.helper.QuoteSource
import com.thing.quoter.helper.QuoterHelper
import com.thing.quoter.R
import com.thing.quoter.databinding.ActivityMainBinding
import com.thing.quoter.databinding.NavigationBinding
import com.thing.quoter.databinding.ToolbarBinding
import com.thing.quoter.fragment.CustomizeFragment
import com.thing.quoter.fragment.QuotePreviewFragment
import com.thing.quoter.fragment.ProviderSelectFragment
import com.thing.quoter.repository.model.Quote
import com.thing.quoter.repository.model.QuoteProvider
import com.thing.quoter.repository.model.QuoteSetting

class MainActivity : AppActivity(), View.OnClickListener,
        CustomizeFragment.OnCustomizeListener,
        ProviderSelectFragment.OnProviderChangedListener,
        CustomizeFragment.OnTextCustomizeListener,
        View.OnLongClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navBar: NavigationBinding
    private lateinit var menuContainer: ToolbarBinding

    override fun onTextCustomize(quoteSetting: QuoteSetting) {
        qpf.loadSetting(quoteSetting)
    }

    val qpf: QuotePreviewFragment = QuotePreviewFragment()
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
                Picasso.get().load(extraStr).into(binding.bg)
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
                    putExtra(Intent.EXTRA_STREAM, getImageUri(getBitmapFromView(binding.rootView)))
                    type = "image/*"
                }
                showMenu(true)
                startActivity(Intent.createChooser(shareIntent, resources.getText(R.string.share_text)))
            }
        }
    }

    fun showMenu(b: Boolean) {
        val v = if (b) View.VISIBLE else View.GONE
        menuContainer.root.visibility = v
        navBar.root.visibility = v
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isFirstTime) {
            theme.applyStyle(R.style.AppTheme_Light, true)
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)

        QuoterHelper.quoteSetting = QuoteSetting()

        menuContainer = binding.menuContainer
        navBar = binding.navBar

        val mainFrameLayout = binding.mainFrameLayout

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.mainFrameLayout, qpf)
                .runOnCommit {
                    if (isFirstTime) {
                        binding.menuContainer.root.visibility = View.GONE
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
                        menuContainer.providerSelect.setOnClickListener(this)
                        menuContainer.customizeSelect.setOnClickListener(this)
                        menuContainer.shareBtn.setOnClickListener(this)
                        mainFrameLayout.setOnClickListener(this)

                        quoteSource?.onEndReachedListener = {
                            qpf.show(it)
                            navBar.rightNavBtn.visibility = View.GONE
                            navBar.leftNavBtn.visibility = View.VISIBLE
                        }
                    }
                    //assigning callbacks
                    navBar.leftNavBtn.setOnClickListener {
                        navLeft()
                    }
                    navBar.rightNavBtn.setOnClickListener {
                        navRight()
                    }
                    quoteSource?.onStartReachedListener = {
                        qpf.show(it)
                        navBar.leftNavBtn.visibility = View.GONE
                        navBar.rightNavBtn.visibility = View.VISIBLE
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

    fun navLeft() {
        val quote = quoteSource?.getPreviousQuote()
        if (quote == null)
            return
        qpf.show(quote)
        navBar.rightNavBtn.visibility = View.VISIBLE
    }

    fun navRight() {
        val quote = quoteSource?.getNextQuote()
        if (quote == null)
            return
        qpf.show(quote)
        navBar.leftNavBtn.visibility = View.VISIBLE
    }

}