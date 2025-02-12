package com.ggaebiz.ggaebiz

import android.app.Application
import com.ggaebiz.ggaebiz.data.datastore.AudioDataStore
import com.ggaebiz.ggaebiz.data.datastore.DataStoreObject
import com.ggaebiz.ggaebiz.di.appModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GgaeBizApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@GgaeBizApp)
            modules(appModule)
        }

        CoroutineScope(Dispatchers.IO).launch {
            val audioDataStore: AudioDataStore = getKoin().get()
            DataStoreObject.initialize(this@GgaeBizApp, audioDataStore)
        }
    }
}