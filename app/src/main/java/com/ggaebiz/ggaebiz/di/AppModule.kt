package com.ggaebiz.ggaebiz.di

import com.ggaebiz.ggaebiz.data.datastore.AudioDataStore
import com.ggaebiz.ggaebiz.data.datastore.DataStoreObject.audioDataStore
import com.ggaebiz.ggaebiz.data.datastore.DataStoreObject.timerDataStore
import com.ggaebiz.ggaebiz.data.datastore.TimerDataStore
import com.ggaebiz.ggaebiz.data.repository.AudioRepositoryImpl
import com.ggaebiz.ggaebiz.data.repository.TimerRepositoryImpl
import com.ggaebiz.ggaebiz.domain.repository.AudioRepository
import com.ggaebiz.ggaebiz.domain.repository.TimerRepository
import com.ggaebiz.ggaebiz.domain.usecase.EndTimerUseCase
import com.ggaebiz.ggaebiz.domain.usecase.GetAudioResIdUseCase
import com.ggaebiz.ggaebiz.domain.usecase.GetCharacterIdxUseCase
import com.ggaebiz.ggaebiz.domain.usecase.GetSnoozeCountUseCase
import com.ggaebiz.ggaebiz.domain.usecase.GetTimerSettingUseCase
import com.ggaebiz.ggaebiz.domain.usecase.SelectCharacterIdxUseCase
import com.ggaebiz.ggaebiz.domain.usecase.SetSnoozeCountUseCase
import com.ggaebiz.ggaebiz.domain.usecase.SetTimerSettingUseCase
import com.ggaebiz.ggaebiz.presentation.ui.alarm.AlarmViewModel
import com.ggaebiz.ggaebiz.presentation.ui.home.HomeViewModel
import com.ggaebiz.ggaebiz.presentation.ui.setting.SettingViewModel
import com.ggaebiz.ggaebiz.presentation.service.TimerServiceManager
import com.ggaebiz.ggaebiz.presentation.ui.timer.TimerViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { androidContext().audioDataStore }
    single { androidContext().timerDataStore }

    single { AudioDataStore(get()) }
    single { TimerDataStore(get()) }

    single<AudioRepository> { AudioRepositoryImpl(get()) }
    single<TimerRepository> { TimerRepositoryImpl(get()) }
    single { TimerServiceManager(androidContext()) }

    factory { GetAudioResIdUseCase(get()) }
    factory { SelectCharacterIdxUseCase(get()) }
    factory { GetCharacterIdxUseCase(get()) }
    factory { SetTimerSettingUseCase(get()) }
    factory { EndTimerUseCase(get()) }
    factory { GetTimerSettingUseCase(get()) }
    factory { SetSnoozeCountUseCase(get()) }
    factory { GetSnoozeCountUseCase(get()) }

    viewModel { HomeViewModel(get()) }
    viewModel { SettingViewModel(get(), get()) }
    viewModel { TimerViewModel(get(), get(), get(),get(), get()) }
    viewModel { AlarmViewModel(get(), get(), get(), get(), get())}
}
