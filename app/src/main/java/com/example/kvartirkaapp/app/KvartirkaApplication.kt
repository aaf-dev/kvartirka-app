package com.example.kvartirkaapp.app

import android.app.Application
import com.example.kvartirkaapp.app.di.AppInjectionModule
import com.example.kvartirkaapp.details.di.DetailsInjectionModule
import com.example.kvartirkaapp.main.di.MainInjectionModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class KvartirkaApplication : Application(), KodeinAware {
    override val kodein: Kodein by Kodein.lazy {
        (applicationContext as KvartirkaApplication).kodein
        import(androidXModule(this@KvartirkaApplication))
        import(AppInjectionModule.module)
        import(MainInjectionModule.module)
        import(DetailsInjectionModule.module)
    }

    companion object {
        const val APPLICATION_LOGGING_TAG = "KV_APP"
    }
}