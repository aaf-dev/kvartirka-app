package com.example.kvartirkaapp.main.di

import com.example.kvartirkaapp.main.api.MainApi
import com.example.kvartirkaapp.main.domain.LocationInteractor
import com.example.kvartirkaapp.main.domain.MainInteractor
import com.example.kvartirkaapp.main.gateway.MainGateway
import com.example.kvartirkaapp.main.ui.MainViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import retrofit2.Retrofit

object MainInjectionModule {
    val module = Kodein.Module(MainInjectionModule.javaClass.name) {
        bind<MainViewModel>() with provider {
            MainViewModel(instance(), instance())
        }

        bind<LocationInteractor>() with singleton {
            LocationInteractor(instance())
        }

        bind<MainInteractor>() with singleton {
            MainInteractor(instance())
        }

        bind<MainGateway>() with singleton {
            MainGateway(instance())
        }

        bind<MainApi>() with singleton {
            val retrofit = instance<Retrofit>()
            retrofit.create(MainApi::class.java)
        }
    }
}