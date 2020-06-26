package com.example.kvartirkaapp.app.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.kvartirkaapp.app.model.HeaderInterceptor
import com.example.kvartirkaapp.extensions.ViewModelFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object AppInjectionModule {
    val module = Kodein.Module(AppInjectionModule.javaClass.name) {
        bind<Context>() with provider {
            instance<Application>()
        }

        bind<ViewModelProvider.Factory>() with singleton {
            ViewModelFactory(instance())
        }

        bind<Retrofit>() with singleton {
            val okHttpClient = instance<OkHttpClient>()

            Retrofit
                .Builder()
                .client(okHttpClient)
                .baseUrl("https://api.beta.kvartirka.pro/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }

        bind<OkHttpClient>() with singleton {
            val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            OkHttpClient.Builder().apply {
                connectTimeout(80, TimeUnit.SECONDS)
                readTimeout(60, TimeUnit.SECONDS)
                retryOnConnectionFailure(true)
                addNetworkInterceptor(httpLoggingInterceptor)
                addNetworkInterceptor(HeaderInterceptor())
            }
                .build()
        }
    }
}