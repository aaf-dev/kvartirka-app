package com.example.kvartirkaapp.details.gateway

import com.example.kvartirkaapp.details.api.DetailsApi
import com.example.kvartirkaapp.main.domain.FlatModel
import com.example.kvartirkaapp.main.domain.Prices
import com.example.kvartirkaapp.utils.Utils
import io.reactivex.Single

class DetailsGateway(
    private val detailsApi: DetailsApi
) {
    fun getFlatById(id: Int): Single<FlatModel> =
        detailsApi
            .getFlatsById(arrayListOf(id).toString())
            .map { response ->
                val flat = response.flats[0]
                FlatModel(
                    id = flat.id,
                    metro = flat.metro,
                    title = flat.title,
                    address = flat.address,
                    url = flat.url,
                    description = flat.description,
                    prices = Prices(
                        pricePerDay = Utils.formatPrice(flat.prices.day, response.currency.label),
                        pricePerNight = Utils.formatPrice(flat.prices.night, response.currency.label),
                        pricePerHour = Utils.formatPrice(flat.prices.hour, response.currency.label)
                    ),
                    defaultPhoto = flat.defaultPhoto,
                    photos = flat.photos
                )
            }

}