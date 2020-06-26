package com.example.kvartirkaapp.main.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kvartirkaapp.R
import com.example.kvartirkaapp.details.ui.DetailsActivity
import com.example.kvartirkaapp.extensions.ViewModelFactory
import com.example.kvartirkaapp.extensions.setOnItemClickListener
import com.example.kvartirkaapp.main.domain.FlatModel
import com.example.kvartirkaapp.recycler.RecyclerViewAdapter
import com.example.kvartirkaapp.utils.Utils.LOCATION_REQUEST_CODE
import com.example.kvartirkaapp.utils.Utils.isLocationPermissionGranted
import com.example.kvartirkaapp.utils.Utils.requestLocationPermission
import com.example.kvartirkaapp.utils.Utils.showMessage
import com.example.kvartirkaapp.utils.Utils.showMessageWithAction
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDispose
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

class MainActivity : AppCompatActivity(), KodeinAware {

    override val kodein: Kodein by kodein()

    private var locationClient: FusedLocationProviderClient? = null

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainProgressBar.visibility = View.VISIBLE

        setupViewModel()
        setupPermission()
    }

    private fun setupViewModel() {
        viewModel = ViewModelFactory(kodein).create(MainViewModel::class.java)
    }

    private fun setupPermission() {
        if (isLocationPermissionGranted(this)) {
            // we got permission
            setupLocationClient()
        } else {
            requestLocationPermission(this)
        }
    }

    private fun setupLocationClient() {
        locationClient = LocationServices.getFusedLocationProviderClient(this)
        getUserLocation()
    }

    private fun getUserLocation() {
        viewModel
            .getUserLocation()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDispose(AndroidLifecycleScopeProvider.from(lifecycle))
            .subscribe(
                {
                    getFlats(it.latitude, it.longitude)
                },
                {
                    Log.e("KVAPP", "Error: ", it)
                }
            )
    }

    private fun getFlats(latitude: Double, longitude: Double) {
        viewModel
            .getFlats(latitude, longitude)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDispose(AndroidLifecycleScopeProvider.from(lifecycle))
            .subscribe(
                {
                    setupRecyclerView(it)
                },
                {
                    Log.e("KVAPP", "Error: ", it)
                }
            )
    }

    private fun setupRecyclerView(flats: List<FlatModel>) {
        val adapter = RecyclerViewAdapter(flats)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        mainProgressBar.visibility = View.GONE

        recycler.setOnItemClickListener {
            val id = adapter.getFlatIdByPosition(it)

            Intent(this, DetailsActivity::class.java).apply {
                putExtra("EXTRA_ID", id)
                startActivity(this)
            }
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
                    setupLocationClient()
                } else {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        showMessage(mainContainer, "Click YES")
                        requestLocationPermission(this)
                    } else {
                        showMessageWithAction(this, mainContainer, "We need location permission!") // TODO: fixme

                    }
                }
            }
        }
    }
}