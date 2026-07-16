package com.ggaebiz.ggaebiz.di

import android.content.Context
import android.media.AudioManager
import com.ggaebiz.ggaebiz.data.datastore.AudioDataStore
import com.ggaebiz.ggaebiz.data.datastore.ConfigDataStore
import com.ggaebiz.ggaebiz.data.datastore.AuthDataStore
import com.ggaebiz.ggaebiz.data.datastore.DataStoreObject.audioDataStore
import com.ggaebiz.ggaebiz.data.datastore.DataStoreObject.authDataStore
import com.ggaebiz.ggaebiz.data.datastore.DataStoreObject.configDataStore
import com.ggaebiz.ggaebiz.data.datastore.DataStoreObject.timerDataStore
import com.ggaebiz.ggaebiz.data.datastore.OnboardingDataStore
import com.ggaebiz.ggaebiz.data.datastore.DataStoreObject.timerRecordDataStore
import com.ggaebiz.ggaebiz.data.datastore.TimerDataStore
import com.ggaebiz.ggaebiz.data.datastore.TimerRecordDataStore
import com.ggaebiz.ggaebiz.data.network.AuthInterceptor
import com.ggaebiz.ggaebiz.data.network.NetworkModule
import com.ggaebiz.ggaebiz.data.repository.AudioRepositoryImpl
import com.ggaebiz.ggaebiz.data.repository.AuthRepositoryImpl
import com.ggaebiz.ggaebiz.data.repository.ConfigRepositoryImpl
import com.ggaebiz.ggaebiz.data.repository.ImageRepositoryImpl
import com.ggaebiz.ggaebiz.data.repository.NicknameRepositoryImpl
import com.ggaebiz.ggaebiz.data.repository.OnboardingRepositoryImpl
import com.ggaebiz.ggaebiz.data.repository.StatisticRepositoryImpl
import com.ggaebiz.ggaebiz.data.repository.TimerRecordRepositoryImpl
import com.ggaebiz.ggaebiz.data.repository.TimerRepositoryImpl
import com.ggaebiz.ggaebiz.domain.repository.AudioRepository
import com.ggaebiz.ggaebiz.domain.repository.AuthRepository
import com.ggaebiz.ggaebiz.domain.repository.ConfigRepository
import com.ggaebiz.ggaebiz.domain.repository.ImageRepository
import com.ggaebiz.ggaebiz.domain.repository.NicknameRepository
import com.ggaebiz.ggaebiz.domain.repository.OnboardingRepository
import com.ggaebiz.ggaebiz.domain.repository.StatisticRepository
import com.ggaebiz.ggaebiz.domain.repository.TimerRecordRepository
import com.ggaebiz.ggaebiz.domain.repository.TimerRepository
import com.ggaebiz.ggaebiz.domain.usecase.CreateCachedImageUseCase
import com.ggaebiz.ggaebiz.domain.usecase.EndTimerUseCase
import com.ggaebiz.ggaebiz.domain.usecase.GetAudioResIdUseCase
import com.ggaebiz.ggaebiz.domain.usecase.GetCharacterIdxUseCase
import com.ggaebiz.ggaebiz.domain.usecase.GetSnoozeCountUseCase
import com.ggaebiz.ggaebiz.domain.usecase.SaveImageToGalleryUseCase
import com.ggaebiz.ggaebiz.domain.usecase.GetCurrentTimerUseCase
import com.ggaebiz.ggaebiz.domain.usecase.GetIsRestCompletedUseCase
import com.ggaebiz.ggaebiz.domain.usecase.GetSettingTimerUseCase
import com.ggaebiz.ggaebiz.domain.usecase.SelectCharacterIdxUseCase
import com.ggaebiz.ggaebiz.domain.usecase.SetSnoozeCountUseCase
import com.ggaebiz.ggaebiz.domain.usecase.SetCurrentTimerUseCase
import com.ggaebiz.ggaebiz.domain.usecase.SetIsRestCompletedUseCase
import com.ggaebiz.ggaebiz.domain.usecase.SetSettingTimerUseCase
import com.ggaebiz.ggaebiz.domain.usecase.SaveTimerRecordUseCase
import com.ggaebiz.ggaebiz.domain.usecase.SendTimerRecordsUseCase
import com.ggaebiz.ggaebiz.domain.usecase.GetTopCardDataUseCase
import com.ggaebiz.ggaebiz.presentation.service.TimerServiceManager
import com.ggaebiz.ggaebiz.presentation.ui.alarm.AlarmViewModel
import com.ggaebiz.ggaebiz.presentation.ui.config.ConfigViewModel
import com.ggaebiz.ggaebiz.presentation.ui.home.HomeViewModel
import com.ggaebiz.ggaebiz.data.auth.KakaoLoginHandler
import com.ggaebiz.ggaebiz.presentation.ui.login.LoginViewModel
import com.ggaebiz.ggaebiz.presentation.ui.onboarding.OnboardingViewModel
import com.ggaebiz.ggaebiz.presentation.ui.proof.card.ProofCardViewModel
import com.ggaebiz.ggaebiz.presentation.ui.statistic.StatisticViewModel
import com.ggaebiz.ggaebiz.presentation.ui.proof.editor.EditorViewModel
import com.ggaebiz.ggaebiz.presentation.ui.proof.finish.EditorResultViewModel
import com.ggaebiz.ggaebiz.presentation.ui.setting.SettingViewModel
import com.ggaebiz.ggaebiz.presentation.ui.splash.SplashViewModel
import com.ggaebiz.ggaebiz.presentation.ui.timer.TimerViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { androidContext().audioDataStore }
    single { androidContext().timerDataStore }
    single { androidContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager }
    single { androidContext().configDataStore }

    single { AudioDataStore(get()) }
    single { TimerDataStore(get()) }
    single { ConfigDataStore(get()) }
    single { OnboardingDataStore(get()) }
    single { androidContext().authDataStore }
    single { AuthDataStore(get()) }
    single { androidContext().timerRecordDataStore }
    single { TimerRecordDataStore(get()) }
    single { AuthInterceptor(get()) }
    single { NetworkModule.provideOkHttpClient(get()) }
    single { NetworkModule.provideRetrofit(get()) }
    single { NetworkModule.provideAuthApi(get()) }
    single { NetworkModule.provideTimerApi(get()) }
    single { NetworkModule.provideStatisticApi(get()) }

    single<AudioRepository> { AudioRepositoryImpl(get()) }
    single<TimerRepository> { TimerRepositoryImpl(get()) }
    single<ConfigRepository> { ConfigRepositoryImpl(get()) }
    single<OnboardingRepository> { OnboardingRepositoryImpl(get()) }
    single<ImageRepository> { ImageRepositoryImpl(appContext = androidContext()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<TimerRecordRepository> { TimerRecordRepositoryImpl(get(), get()) }
    single<StatisticRepository> { StatisticRepositoryImpl(get()) }
    single<NicknameRepository> { NicknameRepositoryImpl(androidContext(), get(), get()) }

    single { TimerServiceManager(androidContext()) }
    single { KakaoLoginHandler() }

    factory { GetAudioResIdUseCase(get()) }
    factory { SelectCharacterIdxUseCase(get()) }
    factory { GetCharacterIdxUseCase(get()) }
    factory { GetCurrentTimerUseCase(get()) }
    factory { SetCurrentTimerUseCase(get()) }
    factory { EndTimerUseCase(get()) }
    factory { GetSettingTimerUseCase(get()) }
    factory { SetSettingTimerUseCase(get()) }
    factory { GetIsRestCompletedUseCase(get()) }
    factory { SetIsRestCompletedUseCase(get()) }
    factory { SetSnoozeCountUseCase(get()) }
    factory { GetSnoozeCountUseCase(get()) }
    factory { CreateCachedImageUseCase(get()) }
    factory { SaveImageToGalleryUseCase(imageRepository = get()) }
    factory { SaveTimerRecordUseCase(get()) }
    factory { SendTimerRecordsUseCase(get()) }
    factory { GetTopCardDataUseCase(get()) }

    viewModel { HomeViewModel(get(), get(), get(), get(), get()) }
    viewModel { SettingViewModel(get(), get(), get(), get(), get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { LoginViewModel(get(), get(), get()) }
    viewModel { OnboardingViewModel(get()) }
    viewModel { TimerViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { AlarmViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { ConfigViewModel(get(), get(), get(), get()) }
    viewModel { EditorViewModel(get(), get()) }
    viewModel { EditorResultViewModel(get(), get()) }
    viewModel { ProofCardViewModel(get(), get(), get(), get(), get()) }
    viewModel { StatisticViewModel(get(), get()) }

}
