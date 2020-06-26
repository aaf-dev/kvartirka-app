package com.example.kvartirkaapp.main.domain

import com.example.kvartirkaapp.main.api.PhotoData

data class FlatModel(
    val id: Int,
    val metro: String,
    val title: String,
    val address: String,
    val url: String,
    val description: String,
    val prices: Prices,
    val defaultPhoto: PhotoData,
    val photos: List<PhotoData>
)

data class Prices(
    val pricePerDay: String,
    val pricePerNight: String,
    val pricePerHour: String
)