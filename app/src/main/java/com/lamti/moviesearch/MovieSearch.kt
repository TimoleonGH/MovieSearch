package com.lamti.moviesearch

import android.app.Application
import com.lamti.moviesearch.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MovieSearch : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MovieSearch)
            modules(appModules)
        }
    }

}