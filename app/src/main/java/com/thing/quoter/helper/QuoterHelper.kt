package com.thing.quoter.helper

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.thing.quoter.repository.model.QuoteProvider
import com.thing.quoter.repository.model.QuoteSetting
import org.greenrobot.eventbus.EventBus
import java.lang.NullPointerException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import kotlin.collections.LinkedHashSet
import kotlin.concurrent.thread

object QuoterHelper {

    private var quoteProvidersRef: CollectionReference = FirebaseFirestore.getInstance().collection("quoteProvider")
    var quoteProviders = FirestoreList(QuoteProvider::class.java, quoteProvidersRef)
    var quoteSetting: QuoteSetting? = null

    var backgrounds = LinkedHashSet<String>()

    private const val TAG = "QuoterHelper"

    var imagesUrl = "https://source.unsplash.com/random"

    private var i = 0

    init {
        //TODO remove
        backgrounds.add("black")
        thread {
            for (i in 1..30) { //FIXME resolve only on scroll
                resolveImageUrls(imagesUrl)
            }
        }
        //FIXME
    }

    class BackgroundUpdateEvent

    /*
           * Resolve urls and stash into background
           */
    private fun resolveImageUrls(backgroundUrlApi: String) {
        try {
            val url = URL(backgroundUrlApi)
            val ucon = url.openConnection() as HttpURLConnection
            ucon.setInstanceFollowRedirects(false)
            val secondURL: URL? = URL(ucon.getHeaderField("Location"))
            backgrounds.add(secondURL.toString())
            //FIXME call event only if  add succeeds
            EventBus.getDefault().post(BackgroundUpdateEvent())
        } catch (npe: NullPointerException) {
            //no-op
        } catch (mue: MalformedURLException) {
            //no-op
        }
    }
}