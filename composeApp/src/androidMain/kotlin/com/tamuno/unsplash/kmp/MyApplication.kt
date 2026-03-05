package com.tamuno.unsplash.kmp

import android.app.Application
import androidx.work.Configuration
import di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.androidx.workmanager.factory.KoinWorkerFactory
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class MyApplication : Application(), Configuration.Provider, KoinComponent {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MyApplication)
            workManagerFactory()
        }
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(get<KoinWorkerFactory>())
            .build()
}