package com.example.kvartirkaapp.main.api

import com.google.gson.annotations.SerializedName

data class FlatResponse(
    val currency: Currency,
    val flats: List<Flat>
)

data class Currency(
    val label: String
)

data class Flat(
    val id: Int,
    val metro: String,
    val title: String,
    val address: String,
    val url: String,

    @SerializedName("description_full")
    val description: String,
    val prices: PriceData,

    @SerializedName("photo_default")
    val defaultPhoto: PhotoData,
    val photos: List<PhotoData>
)

data class PriceData(
    val day: Int,
    val night: Int,
    val hour: Int
)

data class PhotoData(
    val url: String,
    val verified: Boolean
)