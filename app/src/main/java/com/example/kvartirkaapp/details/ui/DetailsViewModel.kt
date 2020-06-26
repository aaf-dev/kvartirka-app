package com.example.kvartirkaapp.details.ui

import androidx.lifecycle.ViewModel
import com.example.kvartirkaapp.details.domain.DetailsInteractor
import com.example.kvartirkaapp.main.domain.FlatModel
import io.reactivex.Single

class DetailsViewModel(
    private val detailsInteractor: DetailsInteractor
) : ViewModel() {
    fun getFlatById(id: Int): Single<FlatModel> =
        detailsInteractor.getFlatById(id)
}