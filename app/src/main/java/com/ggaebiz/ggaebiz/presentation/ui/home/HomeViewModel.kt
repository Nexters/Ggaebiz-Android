package com.ggaebiz.ggaebiz.presentation.ui.home

import android.media.AudioManager
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.domain.repository.ConfigRepository
import com.ggaebiz.ggaebiz.domain.usecase.SelectCharacterIdxUseCase
import com.ggaebiz.ggaebiz.presentation.common.base.BaseViewModel
import com.ggaebiz.ggaebiz.presentation.ui.config.ConfigSideEffect
import kotlinx.coroutines.delay

data class HomeState(
    val selectCharacterIdx: Int = 0,
    val isGranted: Boolean = false,
    val backPressedOnce: Boolean = false,
    val volumeToastStatus : Boolean = false,
    val isNudgeGuideViewed : Boolean = false,
    val isBatteryPopupShow : Boolean = false,
    val nudgeGuideIdx : Int = 1
){
    val homeClickEnable = isNudgeGuideViewed && !isBatteryPopupShow
}

sealed interface HomeSideEffect {
    data object NavigateToSetting : HomeSideEffect
    data object NavigateToConfig : HomeSideEffect
    data object CheckPermission : HomeSideEffect
    data class ShowToast(val message : Int) : HomeSideEffect
    data object FinishApp : HomeSideEffect
    data object MoveToDeviceSetting : HomeSideEffect
}

sealed interface HomeIntent {
    data class SelectCharacter(val selectCharacterIdx: Int) : HomeIntent
    data class UpdatePermission(val isGranted: Boolean) : HomeIntent
    data object EnterScreen : HomeIntent
    data object ClickSettingButton : HomeIntent
    data object PlayMentAudio : HomeIntent
    data object ClickConfigButton : HomeIntent
    data object PressedBack : HomeIntent
    data object ClickNudgeSkipButton : HomeIntent
    data object ClickNudgeConfirmButton : HomeIntent
    data object CheckBatteryPopUp : HomeIntent
    data object ClickBatteryNextButton : HomeIntent
    data object ClickBatteryMoveButton : HomeIntent
}

class HomeViewModel(
    private val selectCharacterIdxUseCase: SelectCharacterIdxUseCase,
    private val audioManager: AudioManager,
    private val configRepository: ConfigRepository
) : BaseViewModel<HomeState, HomeIntent, HomeSideEffect>(HomeState()) {

    private val deviceVolume get() = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

    fun processIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.ClickSettingButton -> {
                clickSettingButton()
            }
            is HomeIntent.SelectCharacter -> {
                selectCharacter(intent.selectCharacterIdx)
            }
            is HomeIntent.UpdatePermission -> {
                updateState { it.copy(isGranted = intent.isGranted) }
            }
            is HomeIntent.PlayMentAudio -> {
                if (deviceVolume == 0) {
                    updateState { it.copy(volumeToastStatus = true) }
                    launch {
                        delay(2000)
                        updateState { it.copy(volumeToastStatus = false) }
                    }
                }
            }
            HomeIntent.ClickConfigButton -> {
               postSideEffect(HomeSideEffect.NavigateToConfig)
            }
            HomeIntent.PressedBack ->{
                if (uiState.value.backPressedOnce){
                    postSideEffect(HomeSideEffect.FinishApp)
                }else{
                    updateState { it.copy(backPressedOnce = true) }
                    postSideEffect(HomeSideEffect.ShowToast(R.string.back_provider_toast_text))
                    launch {
                        delay(2000)
                        updateState { it.copy(backPressedOnce = false) }
                    }
                }
            }
            HomeIntent.ClickNudgeConfirmButton -> {
                if (uiState.value.nudgeGuideIdx == 1){
                    updateState { it.copy(nudgeGuideIdx = 2) }
                }else{
                    finishHomeNudge()
                }
            }
            HomeIntent.ClickNudgeSkipButton -> {
                finishHomeNudge()
            }
            HomeIntent.EnterScreen -> {
                launch{
                    if (configRepository.getHomeNudgeGuideViewed()){
                        updateState { it.copy(isNudgeGuideViewed = true) }
                        postSideEffect(HomeSideEffect.CheckPermission)
                    }
                }
            }
            HomeIntent.ClickBatteryMoveButton ->launch {
                updateState { it.copy(isBatteryPopupShow = false) }
                configRepository.setBatteryPopupViewed(true)
                postSideEffect(HomeSideEffect.MoveToDeviceSetting)
            }
            HomeIntent.ClickBatteryNextButton -> launch{
                updateState { it.copy(isBatteryPopupShow = false) }
                configRepository.setBatteryPopupViewed(true)
            }
            HomeIntent.CheckBatteryPopUp -> launch{
                if (!configRepository.getBatteryPopupViewed()){
                    updateState { it.copy(isBatteryPopupShow = true) }
                }
            }
        }
    }

    private fun selectCharacter(selectCharacterIdx: Int) = launch {
        updateState { it.copy(selectCharacterIdx = selectCharacterIdx) }
    }

    private fun clickSettingButton() = launch {
        selectCharacterIdxUseCase(uiState.value.selectCharacterIdx)
        postSideEffect(HomeSideEffect.NavigateToSetting)
    }

    private fun finishHomeNudge() = launch{
        configRepository.setHomeNudgeGuideViewed(true)
        updateState { it.copy(isNudgeGuideViewed = true) }
        postSideEffect(HomeSideEffect.CheckPermission)
    }
}
