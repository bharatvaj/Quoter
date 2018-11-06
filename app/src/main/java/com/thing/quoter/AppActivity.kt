package com.thing.quoter

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity

@SuppressLint("Registered")
open class AppActivity : AppCompatActivity() {

    var isFirstTime = true
    var isDark = false
    var isFontSerif = true


    companion object {
        const val WRITE_EXT_REQUEST_CODE = 1

        const val SWIPE_MIN_DISTANCE = 120
        const val SWIPE_THRESHOLD_VELOCITY = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isFirstTime) {
            val preferences = getSharedPreferences(getString(R.string.settings_pref_file), Context.MODE_PRIVATE)
            isFirstTime = preferences.getBoolean(getString(R.string.settings_isFirstTime), true)
            isDark = preferences.getBoolean(getString(R.string.settings_isDark), true)
            isFontSerif = preferences.getBoolean(getString(R.string.settings_isFontSerif), true)

            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            WRITE_EXT_REQUEST_CODE)

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                // Permission has already been granted
            }
        }

    }

    fun toggleTheme() {
        isDark = !isDark
        recreate()
    }

    fun toggleFont() {
        isFontSerif = !isFontSerif
        recreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        val editor = getSharedPreferences(getString(R.string.settings_pref_file), Context.MODE_PRIVATE).edit()
        editor.putBoolean(getString(R.string.settings_isFirstTime), isFirstTime)
        editor.putBoolean(getString(R.string.settings_isDark), isDark)
        editor.putBoolean(getString(R.string.settings_isFontSerif), isFontSerif)
        editor.apply()
    }
}
