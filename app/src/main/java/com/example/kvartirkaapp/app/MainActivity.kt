package com.example.kvartirkaapp.app

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.example.kvartirkaapp.R
import com.example.kvartirkaapp.main.ui.MainFragment
import com.example.kvartirkaapp.utils.Utils.LOCATION_REQUEST_CODE
import com.example.kvartirkaapp.utils.Utils.isLocationPermissionGranted
import com.example.kvartirkaapp.utils.Utils.requestLocationPermission
import com.example.kvartirkaapp.utils.Utils.showMessage
import com.example.kvartirkaapp.utils.Utils.showMessageWithAction
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

class MainActivity : AppCompatActivity(), KodeinAware {

    override val kodein: Kodein by kodein()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupPermission()
    }

    private fun setupPermission() {
        if (isLocationPermissionGranted(this)) {
            showMainFragment()
        } else {
            requestLocationPermission(this)
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showMainFragment()
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    ) {
                        showMessage(
                            layoutMainActivity,
                            getString(R.string.snackbar_message_without_action)
                        )
                        requestLocationPermission(this)
                    } else {
                        showMessageWithAction(
                            this,
                            layoutMainActivity,
                            getString(R.string.snackbar_message_with_action)
                        )

                    }
                }
            }
        }
    }

    private fun showMainFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer,
                MainFragment(), MainFragment().javaClass.name)
            .addToBackStack(null)
            .commit()
    }
}