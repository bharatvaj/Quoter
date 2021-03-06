package com.thing.quoter

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.gunhansancar.changelanguageexample.helper.LocaleHelper
import java.io.ByteArrayOutputStream

abstract class AppActivity : AppCompatActivity() {

    var isFirstTime = true
    var isDark = false


    companion object {
        const val WRITE_EXT_REQUEST_CODE = 1

        const val SWIPE_MIN_DISTANCE = 120
        const val SWIPE_THRESHOLD_VELOCITY = 200
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    fun getBitmapFromView(view: View): Bitmap {
        val b = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(b)
        view.draw(canvas)
        return b
    }

    fun getImageUri(b: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        b.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(contentResolver, b, "quoter_image", "")
        return Uri.parse(path)
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            WRITE_EXT_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                } else {
//                    TODO show("Cannot save until you give permissions")
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isFirstTime) {
            val preferences = getSharedPreferences(getString(R.string.settings_pref_file), Context.MODE_PRIVATE)
            isFirstTime = preferences.getBoolean(getString(R.string.settings_isFirstTime), true)
            isDark = preferences.getBoolean(getString(R.string.settings_isDark), true)

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

    override fun onDestroy() {
        super.onDestroy()
        val editor = getSharedPreferences(getString(R.string.settings_pref_file), Context.MODE_PRIVATE).edit()
        editor.putBoolean(getString(R.string.settings_isFirstTime), isFirstTime)
        editor.putBoolean(getString(R.string.settings_isDark), isDark)
        editor.apply()
    }
}
