package com.example.kvartirkaapp.details.di

import com.example.kvartirkaapp.details.api.DetailsApi
import com.example.kvartirkaapp.details.domain.DetailsInteractor
import com.example.kvartirkaapp.details.gateway.DetailsGateway
import com.example.kvartirkaapp.details.ui.DetailsViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import retrofit2.Retrofit

object DetailsInjectionModule {
    val module = Kodein.Module(DetailsInjectionModule.javaClass.name) {
        bind<DetailsViewModel>() with provider {
            DetailsViewModel(instance())
        }

        bind<DetailsInteractor>() with singleton {
            DetailsInteractor(instance())
        }

        bind<DetailsGateway>() with singleton {
            DetailsGateway(instance())
        }

        bind<DetailsApi>() with singleton {
            val retrofit = instance<Retrofit>()
            retrofit.create(DetailsApi::class.java)
        }
    }
}