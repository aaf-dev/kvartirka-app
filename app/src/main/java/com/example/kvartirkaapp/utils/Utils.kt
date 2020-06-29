package com.example.kvartirkaapp.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.view.View
import androidx.core.app.ActivityCompat
import com.example.kvartirkaapp.R
import com.google.android.material.snackbar.Snackbar
import java.text.DecimalFormat

object Utils {

    const val LOCATION_REQUEST_CODE = 1

    fun isLocationPermissionGranted(context: Context): Boolean =
        ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    fun requestLocationPermission(context: Context) {
        if (!isLocationPermissionGranted(context)) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
        }
    }

    fun showMessage(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

    fun showMessageWithAction(context: Context, view: View, message: String) {
        val clickListener = View.OnClickListener {
            Intent().apply {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data = Uri.fromParts("package", context.packageName, null)
                context.startActivity(this)
            }
        }
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).apply {
            setAction(context.getString(R.string.setting_action), clickListener)
            show()
        }
    }

    fun formatPrice(price: Int, currency: String): String {
        val formatter = DecimalFormat("#,###")
        val symbols = formatter.decimalFormatSymbols

        symbols.groupingSeparator = ' '
        formatter.decimalFormatSymbols = symbols

        return if (price != 0) {
            "${formatter.format(price)} $currency"
        } else {
            "—"
        }
    }
}