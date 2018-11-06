package com.thing.quoter

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.gunhansancar.changelanguageexample.helper.LocaleHelper
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import com.thing.quoter.model.Quote
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.footer_section.*
import kotlinx.android.synthetic.main.header_section.*
import java.io.ByteArrayOutputStream


class MainActivity : AppActivity(), View.OnClickListener, View.OnLongClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imageQuoteContainer -> {
                show(QuoterHelper.getQuote())
            }
            R.id.themeToggle -> {
                toggleTheme()
            }
            R.id.fontToggle -> {
                //TODO change font app globally
                toggleFont()
            }
            R.id.providerSelect -> {
                //QuoterHelper operations, open the selections fragment
            }
            R.id.toggleSpeaker -> {
                speakerTextView.visibility = if (speakerTextView.visibility == View.VISIBLE) View.GONE else View.VISIBLE
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

    override fun onLongClick(v: View?): Boolean {
        when (v!!.id) {
            R.id.imageQuoteContainer -> {
                if (!isDark) {
                    QuoterHelper.shouldLoadImage = true
                    toggleTheme()
                    return false
                }
                loadImage()
                return true
            }
            else -> {
                return true
            }
        }
    }


    fun indicateLoading(shouldLoad: Boolean, ofBackground: Boolean = true, quote: Quote? = null) {
        if (shouldLoad) {
            if (bg.tag == null) {
                bg.setImageDrawable(resources.getDrawable(R.drawable.unsplash, theme))
                bg.tag = ""
            }
            if (ofBackground) {
                val animation = AnimationUtils.loadAnimation(this, android.R.anim.fade_out)
                animation.repeatCount = Animation.INFINITE
                animation.repeatMode = Animation.REVERSE
                bg.startAnimation(animation)
            } else {
                if (quote != null) {
                    quoteTextView.text = quote.quote
                    speakerTextView.text = quote.speaker
                    val animation = AnimationUtils.loadAnimation(this, android.R.anim.fade_out)
                    animation.repeatCount = Animation.INFINITE
                    animation.repeatMode = Animation.REVERSE
                    quoteContainer.startAnimation(animation)
                }
            }
        } else {
            if (ofBackground) {
                bg.clearAnimation()
            } else {
                quoteContainer.clearAnimation()
            }
        }
    }

    private fun loadImage() {
        indicateLoading(true)
        Picasso.get()
                .load(QuoterHelper.imagesUrl)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .noPlaceholder()
                .into(object : Target {
                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
                    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                        indicateLoading(false)
                    }

                    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                        indicateLoading(false)
                        bg.setImageBitmap(bitmap)
                        bg.imageAlpha = 180
                    }
                })
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    fun show(quote: Quote?) {
        if (quote == null) return show(resources.getString(R.string.quote_loading))
        quoteContainer.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in).apply {
            interpolator = FastOutSlowInInterpolator()
        })
        quoteTextView.text = quote.quote
        speakerTextView.text = if (quote.speaker.isEmpty()) getString(R.string.speaker_unknown) else quote.speaker
    }

    //Replacement for Toast
    fun show(message: String, speaker: String = getString(R.string.app_name)) {
        show(Quote(message, speaker))
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

    fun getBitmapFromView(view: View): Bitmap {
        var b = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(b)
        menuContainer.visibility = View.GONE
        view.draw(canvas)
        menuContainer.visibility = View.VISIBLE
        return b
    }


    fun getImageUri(b: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        b.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(contentResolver, b, "Title", "")
        return Uri.parse(path)
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            WRITE_EXT_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                } else {
                    show("Cannot save until you give permissions")
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        when (v!!.id) {
            R.id.providerSelect -> {
                menu!!.setHeaderTitle("Languages")
                val l = menu.addSubMenu("English")
                l.setHeaderTitle("English")
                l.add("goodreads")
                val t = menu.addSubMenu("Tamil")
                t.setHeaderTitle("Tamil")
                t.add("Tirukural")
            }
        }
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        show("Fun")
        return super.onContextItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        theme.applyStyle(if (isDark) R.style.AppTheme else R.style.AppTheme_Light, true)
        setContentView(R.layout.activity_main)
        //
        quoteTextView.typeface = if (isFontSerif) Typeface.SERIF else Typeface.SANS_SERIF
        //

        registerForContextMenu(providerSelect)

        if (isFirstTime) {
            menuContainer.visibility = View.GONE
            return onboard()
        } else {
            show(QuoterHelper.stashedQuote)
            //then register click
            imageQuoteContainer.setOnClickListener(this)
            //register long press
            imageQuoteContainer.setOnLongClickListener(this)
            //setup share fling
            val gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
                override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
                    if (e1?.y!! - e2?.y!! > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                        //share()
                    }
                    return true
                }
            })
            imageQuoteContainer.setOnTouchListener { _, motionEvent ->
                gestureDetector.onTouchEvent(motionEvent)
            }
            themeToggle.setOnClickListener(this)
            fontToggle.setOnClickListener(this)
            providerSelect.setOnClickListener(this)
            toggleSpeaker.setOnClickListener(this)
            shareBtn.setOnClickListener(this)
        }
        if (QuoterHelper.shouldLoadImage) {
            loadImage()
            QuoterHelper.shouldLoadImage = false
        }
    }
}