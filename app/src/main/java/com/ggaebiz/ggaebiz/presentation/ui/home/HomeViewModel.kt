package com.ggaebiz.ggaebiz.presentation.ui.home

import android.media.AudioManager
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.domain.repository.OnboardingRepository
import com.ggaebiz.ggaebiz.domain.usecase.SelectCharacterIdxUseCase
import com.ggaebiz.ggaebiz.presentation.common.base.BaseViewModel
import kotlinx.coroutines.delay

data class HomeState(
    val selectCharacterIdx: Int = 0,
    val isNotificationGranted: Boolean = false,
    val backPressedOnce: Boolean = false,
    val volumeToastStatus : Boolean = false,
    val isNudgeGuideViewed : Boolean = false,
    val isBatteryPopupShow : Boolean = false,
    val nudgeGuideIdx : Int = 1,
    val isProofPopupShow : Boolean = false, // 테스트를 위해선 이부분을 ture로 바꾸어주세욥
    val isChoiceWayPopup : Boolean = false,
    val proofImageUri : Uri? = null
){
    val homeClickEnable = isNudgeGuideViewed && !isBatteryPopupShow
}

sealed interface HomeSideEffect {
    data object NavigateToSetting : HomeSideEffect
    data object NavigateToConfig : HomeSideEffect
    data object CheckNotificationsPermission : HomeSideEffect
    data class ShowToast(val message : Int) : HomeSideEffect
    data object FinishApp : HomeSideEffect
    data object MoveToDeviceSetting : HomeSideEffect
    data object MoveToCamera : HomeSideEffect
    data object MoveToGallery : HomeSideEffect
    data class MoveToProof(val uri : Uri) : HomeSideEffect
}

sealed interface HomeIntent {
    data class SelectCharacter(val selectCharacterIdx: Int) : HomeIntent
    data class UpdateNotificationPermission(val isGranted: Boolean) : HomeIntent
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

    data object ClickProofDisMissButton : HomeIntent
    data object ClickProofMoveButton : HomeIntent
    data object ClickProofCamera : HomeIntent
    data object ClickProofGallery : HomeIntent
    data object ClickProofCard : HomeIntent

    data class FinishGetImage(val uri : Uri?) : HomeIntent
}


class HomeViewModel(
    savedStateHandle: SavedStateHandle,
    private val selectCharacterIdxUseCase: SelectCharacterIdxUseCase,
    private val audioManager: AudioManager,
    private val onboardingRepository: OnboardingRepository
) : BaseViewModel<HomeState, HomeIntent, HomeSideEffect>(HomeState(
    isProofPopupShow = savedStateHandle.get<Boolean>("isFromAlarm") ?: false
)) {

    private val deviceVolume get() = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)


    fun processIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.ClickSettingButton -> {
                clickSettingButton()
            }
            is HomeIntent.SelectCharacter -> {
                selectCharacter(intent.selectCharacterIdx)
            }
            is HomeIntent.UpdateNotificationPermission -> {
                updateState { it.copy(isNotificationGranted = intent.isGranted) }
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
                    if (onboardingRepository.getHomeNudgeGuideViewed()){
                        updateState { it.copy(isNudgeGuideViewed = true) }
                        postSideEffect(HomeSideEffect.CheckNotificationsPermission)
                    }
                }
            }
            HomeIntent.ClickBatteryMoveButton ->launch {
                updateState { it.copy(isBatteryPopupShow = false) }
                onboardingRepository.setBatteryPopupViewed(true)
                postSideEffect(HomeSideEffect.MoveToDeviceSetting)
            }
            HomeIntent.ClickBatteryNextButton -> launch{
                updateState { it.copy(isBatteryPopupShow = false) }
                onboardingRepository.setBatteryPopupViewed(true)
            }
            HomeIntent.CheckBatteryPopUp -> launch{
                if (!onboardingRepository.getBatteryPopupViewed()){
                    updateState { it.copy(isBatteryPopupShow = true) }
                }
            }
            HomeIntent.ClickProofDisMissButton -> {
                updateState { it.copy(isProofPopupShow = false) }
            }
            HomeIntent.ClickProofMoveButton -> { updateState { it.copy(isProofPopupShow = false, isChoiceWayPopup = true) } }
            HomeIntent.ClickProofCamera -> { postSideEffect(HomeSideEffect.MoveToCamera) }
            HomeIntent.ClickProofGallery -> { postSideEffect(HomeSideEffect.MoveToGallery)}
            HomeIntent.ClickProofCard -> { }
            is HomeIntent.FinishGetImage -> {
                updateState { it.copy(isChoiceWayPopup = false) }
                intent.uri?.let {  postSideEffect(HomeSideEffect.MoveToProof(it))}
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
        onboardingRepository.setHomeNudgeGuideViewed(true)
        updateState { it.copy(isNudgeGuideViewed = true) }
        postSideEffect(HomeSideEffect.CheckNotificationsPermission)
    }
}
