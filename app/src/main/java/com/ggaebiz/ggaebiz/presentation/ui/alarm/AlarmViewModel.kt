package com.ggaebiz.ggaebiz.presentation.ui.alarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ggaebiz.ggaebiz.data.model.Character
import com.ggaebiz.ggaebiz.domain.usecase.GetAudioResIdUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AlarmState(
    val text: String = "타이머를 울려주세요",
)

sealed interface AlarmSideEffect {
    data class PlayAudio(val resId: String) : AlarmSideEffect
}

class AlarmViewModel(
    private val getAudioResIdUseCase: GetAudioResIdUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlarmState())
    val uiState: StateFlow<AlarmState> = _uiState.asStateFlow()

    private val _sideEffects = Channel<AlarmSideEffect>()
    val sideEffects: Flow<AlarmSideEffect> = _sideEffects.receiveAsFlow()


    fun startTimer(
        character: Character,
        level: Int,
    ) = viewModelScope.launch {
        val resIds = getAudioResIdUseCase(character, level, 0)
        _uiState.update { it.copy(text = "타이머 울리는 중") }
        _sideEffects.send(AlarmSideEffect.PlayAudio(resIds))
    }
}
