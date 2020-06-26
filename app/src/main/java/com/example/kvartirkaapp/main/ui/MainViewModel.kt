package com.example.kvartirkaapp.main.ui

import android.location.Location
import androidx.lifecycle.ViewModel
import com.example.kvartirkaapp.main.domain.FlatModel
import com.example.kvartirkaapp.main.domain.LocationInteractor
import com.example.kvartirkaapp.main.domain.MainInteractor
import io.reactivex.Maybe
import io.reactivex.Single

class MainViewModel(
    private val mainInteractor: MainInteractor,
    private val locationInteractor: LocationInteractor
) : ViewModel() {

    fun getUserLocation(): Maybe<Location> =
        locationInteractor.getUserLocation()

    fun getFlats(latitude: Double, longitude: Double): Single<List<FlatModel>> =
        mainInteractor.getFlats(latitude, longitude)
}