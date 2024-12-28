package com.example.note.app

import android.app.Application
import com.example.note.di.appModule
import com.example.note.di.dataModule
import com.example.note.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(listOf(
                appModule,
                domainModule,
                dataModule
            ))
        }
    }
}