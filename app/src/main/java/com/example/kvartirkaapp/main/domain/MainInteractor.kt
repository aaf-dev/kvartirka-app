package com.example.kvartirkaapp.main.domain

import com.example.kvartirkaapp.main.gateway.MainGateway
import io.reactivex.Single

class MainInteractor(private val mainGateway: MainGateway) {

    fun getFlats(latitude: Double, longitude: Double): Single<List<FlatModel>> =
        mainGateway.getFlats(latitude, longitude)
}