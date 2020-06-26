package com.example.kvartirkaapp.details.domain

import com.example.kvartirkaapp.details.gateway.DetailsGateway
import com.example.kvartirkaapp.main.domain.FlatModel
import io.reactivex.Single

class DetailsInteractor(
    private val detailsGateway: DetailsGateway
) {
    fun getFlatById(id: Int): Single<FlatModel> =
        detailsGateway.getFlatById(id)
}