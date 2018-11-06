package com.thing.quoter

import android.app.Service
import android.content.Intent
import android.os.IBinder

class QuoterDownloader : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Download quotes and images")

        //TODO save quotes in json file
//        val client = OkHttpClient()
//        thread {
//            val request = Request.Builder()
//                    .url("http://quotes.rest/qod.json")
//                    .build()
//
//            val response = client.newCall(request).execute()
//
//            quotes.add(Quote(response.body()?.string() ?: "NULL", "Quoter"))
//        }
        //TODO start looking for files continuosly for quoes (or hook service with MainActivity?)
        //TODO provide a callback to hook into MainActivity's UI
    }
}
