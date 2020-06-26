package com.example.kvartirkaapp.details.api

import com.example.kvartirkaapp.main.api.FlatResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface DetailsApi {

    @GET("/client/1.4/flats/")
    fun getFlatsById(
        @Query("flat_ids") flatIds: String
    ): Single<FlatResponse>
}