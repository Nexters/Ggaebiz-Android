package com.ggaebiz.ggaebiz.di

import com.ggaebiz.ggaebiz.data.datastore.AudioDataStore
import com.ggaebiz.ggaebiz.data.datastore.DataStoreObject.audioDataStore
import com.ggaebiz.ggaebiz.data.datastore.DataStoreObject.timerDataStore
import com.ggaebiz.ggaebiz.data.datastore.TimerDataStore
import com.ggaebiz.ggaebiz.data.repository.AudioRepositoryImpl
import com.ggaebiz.ggaebiz.domain.repository.AudioRepository
import com.ggaebiz.ggaebiz.domain.usecase.GetAudioResIdUseCase
import com.ggaebiz.ggaebiz.presentation.ui.alarm.AlarmViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { androidContext().audioDataStore }
    single { androidContext().timerDataStore }

    single { AudioDataStore(get()) }
    single { TimerDataStore(get()) }

    single<AudioRepository> { AudioRepositoryImpl(get()) }
    factory { GetAudioResIdUseCase(get()) }
    viewModel { AlarmViewModel(get()) }
}
