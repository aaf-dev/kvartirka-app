package com.example.kvartirkaapp.main.api

import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query

interface MainApi {

    @GET("/client/1.4/flats/")
    fun getFlats(
        @Query("point_lat") latitude: Double,
        @Query("point_lng") longitude: Double
    ): Single<FlatResponse>
}