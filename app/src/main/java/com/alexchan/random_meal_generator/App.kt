package com.alexchan.random_meal_generator

import android.app.Application
import com.alexchan.random_meal_generator.di.apiModule
import com.alexchan.random_meal_generator.di.networkModule
import com.alexchan.random_meal_generator.di.repositoryModule
import com.alexchan.random_meal_generator.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(viewModelModule, repositoryModule, apiModule, networkModule)
        }
    }
}