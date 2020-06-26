package com.example.kvartirkaapp.main.domain

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.patloew.rxlocation.RxLocation
import io.reactivex.Maybe

class LocationInteractor(private val context: Context) {

    @SuppressLint("MissingPermission")
    fun getUserLocation(): Maybe<Location> =
        RxLocation(context)
            .location()
            .lastLocation()
}