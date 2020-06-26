package com.example.kvartirkaapp.app.model

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain
            .request()
            .newBuilder()
            .addHeader("X-Client-ID", "test")
            .addHeader("X-Device-ID", "test")
            .addHeader("X-Device-OS", "10")
            .addHeader("X-ApplicationVersion", "test")
            .build()

        return chain.proceed(request)
    }
}