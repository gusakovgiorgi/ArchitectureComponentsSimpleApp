package com.example.simpleappwithmoderntechnologies

import android.app.Application
import com.example.simpleappwithmoderntechnologies.di.appModule
import com.example.simpleappwithmoderntechnologies.di.networkModule
import com.example.simpleappwithmoderntechnologies.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AppInstance : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@AppInstance)
            modules(listOf(appModule, viewModelModule, networkModule))
        }
    }
}