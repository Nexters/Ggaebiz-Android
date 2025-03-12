package com.ggaebiz.ggaebiz.presentation.ui.home

import android.media.AudioManager
import com.ggaebiz.ggaebiz.domain.usecase.SelectCharacterIdxUseCase
import com.ggaebiz.ggaebiz.presentation.common.base.BaseViewModel

data class HomeState(
    val selectCharacterIdx: Int = 0,
    val isGranted: Boolean = false,
)

sealed interface HomeSideEffect {
    data object NoticeVolumeOff : HomeSideEffect
    data object NavigateToSetting : HomeSideEffect
    data object CheckPermission : HomeSideEffect
}

sealed interface HomeIntent {
    data class SelectCharacter(val selectCharacterIdx: Int) : HomeIntent
    data object ClickSettingButton : HomeIntent
    data class UpdatePermission(val isGranted: Boolean) : HomeIntent
    data object PlayMentAudio : HomeIntent
}

class HomeViewModel(
    private val selectCharacterIdxUseCase: SelectCharacterIdxUseCase,
    private val audioManager: AudioManager,
) : BaseViewModel<HomeState, HomeIntent, HomeSideEffect>(HomeState()) {

    private val deviceVolume get() = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

    init {
        postSideEffect(HomeSideEffect.CheckPermission)
    }

    fun processIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.ClickSettingButton -> clickSettingButton()
            is HomeIntent.SelectCharacter -> selectCharacter(intent.selectCharacterIdx)
            is HomeIntent.UpdatePermission -> updateState { it.copy(isGranted = intent.isGranted) }
            is HomeIntent.PlayMentAudio -> checkVolume()
        }
    }

    private fun selectCharacter(selectCharacterIdx: Int) = launch {
        updateState { it.copy(selectCharacterIdx = selectCharacterIdx) }
    }

    private fun clickSettingButton() = launch {
        selectCharacterIdxUseCase(uiState.value.selectCharacterIdx)
        postSideEffect(HomeSideEffect.NavigateToSetting)
    }

    private fun checkVolume() {
        if (deviceVolume == 0) {
            postSideEffect(HomeSideEffect.NoticeVolumeOff)
        }
    }
}
