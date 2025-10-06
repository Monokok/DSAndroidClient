package com.yeah.dsapp

import android.app.Application
import android.util.Log
import com.yeah.dsapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Инициализация Koin
        startKoin {
            androidLogger() // включение логирования для Koin
            androidContext(this@MyApplication)
            modules(appModule)
        }
        Log.d("KoinInit", "Koin запущен!")

    }
}