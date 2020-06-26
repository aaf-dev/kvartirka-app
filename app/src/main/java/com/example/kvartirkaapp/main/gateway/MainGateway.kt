package com.example.kvartirkaapp.main.gateway

import com.example.kvartirkaapp.main.api.MainApi
import com.example.kvartirkaapp.main.domain.FlatModel
import com.example.kvartirkaapp.main.domain.Prices
import com.example.kvartirkaapp.utils.Utils.formatPrice
import io.reactivex.Single

class MainGateway(private val mainApi: MainApi) {

    fun getFlats(latitude: Double, longitude: Double): Single<List<FlatModel>> =
        mainApi
            .getFlats(latitude, longitude)
            .map { response ->
                val flats = mutableListOf<FlatModel>()
                response.flats.forEach {
                    flats.add(
                        FlatModel(
                            id = it.id,
                            metro = it.metro,
                            title = it.title,
                            address = it.address,
                            url = it.url,
                            description = it.description,
                            prices = Prices(
                                pricePerDay = formatPrice(it.prices.day, response.currency.label),
                                pricePerNight = formatPrice(it.prices.day, response.currency.label),
                                pricePerHour = formatPrice(it.prices.day, response.currency.label)
                            ),
                            defaultPhoto = it.defaultPhoto,
                            photos = it.photos
                        )
                    )
                }
                flats
            }
}