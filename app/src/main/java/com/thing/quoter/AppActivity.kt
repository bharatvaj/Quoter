package com.thing.quoter

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

@SuppressLint("Registered")
open class AppActivity : AppCompatActivity() {

    private var isDark = false
    private var initialized = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!initialized) {
            val preferences = getSharedPreferences(getString(R.string.settings_pref_file), Context.MODE_PRIVATE)
            isDark = preferences.getBoolean(getString(R.string.settings_isDark), true)
        }
        setTheme(if (isDark) R.style.AppTheme else R.style.AppTheme_Light)
        initialized = true
    }

    @SuppressLint("ApplySharedPref")
    fun toggleTheme() {
        isDark = !isDark
        recreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        val editor = getSharedPreferences(getString(R.string.settings_pref_file), Context.MODE_PRIVATE).edit()
        editor.putBoolean(getString(R.string.settings_isDark), isDark)
        editor.apply()
    }
}
