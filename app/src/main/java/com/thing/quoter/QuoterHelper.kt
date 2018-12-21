package com.thing.quoter

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.thing.quoter.model.Quote
import com.thing.quoter.model.QuoteProvider
import org.greenrobot.eventbus.EventBus
import java.lang.NullPointerException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashSet
import kotlin.concurrent.thread

object QuoterHelper {
    private var quotesRef: CollectionReference = FirebaseFirestore.getInstance().collection("quotes")
    private var quoteProvidersRef: CollectionReference = FirebaseFirestore.getInstance().collection("quoteProvider")
    private var quotes = FirestoreList(Quote::class.java, quotesRef)
    var quoteProviders = FirestoreList(QuoteProvider::class.java, quoteProvidersRef)
    var backgrounds = LinkedHashSet<String>()
    private var randomIndex = Random()

    private const val TAG = "QuoterHelper"

    private var quoteList = ArrayList<Quote>()

    var imagesUrl = "https://source.unsplash.com/random"
    var shouldLoadImage = false
    var stashedQuote: Quote? = null
    var stashedImage: String? = null
    private var i = 0

    init {
        //TODO remove
//        backgrounds.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSgTDOzadSH7HZlU96Ogy2XzMynwk4o4Epcvw12FmpBYRT0B_aLrA")
        //populate background resources into backgrounds arraylist
        thread {
            for (i in 1..30) { //FIXME resolve only on scroll
                resolveImageUrls(imagesUrl)
            }
        }
    }

    class MessageEvent /* Additional fields if needed */

    /*
           * Resolve urls and stash into background
           */
    private fun resolveImageUrls(backgroundUrlApi: String) {
        try {
            var url = URL(backgroundUrlApi)
            var ucon = url.openConnection() as HttpURLConnection
            ucon.setInstanceFollowRedirects(false)
            var secondURL: URL? = URL(ucon.getHeaderField("Location"))
            backgrounds.add(secondURL.toString())
            //FIXME call event only if  add succeeds
            EventBus.getDefault().post(MessageEvent())
        } catch (npe: NullPointerException) {
            //no-op
        } catch (mue: MalformedURLException) {
            //no-op
        }
    }


    fun getQuote(): Quote? {
        stashedQuote = if (quotes.isEmpty) null else quotes.keyAt(i++ % quotes.size)
        return stashedQuote
    }
}