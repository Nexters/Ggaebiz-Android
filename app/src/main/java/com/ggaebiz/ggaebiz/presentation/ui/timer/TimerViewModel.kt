package com.ggaebiz.ggaebiz.presentation.ui.timer

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

data class TimerState(
    val text: String = "타이머를 울려주세요",
)

sealed interface TimerSideEffect {
    data class PlayAudio(val resId: String) : TimerSideEffect
}

class TimerViewModel(
    private val getAudioResIdUseCase: GetAudioResIdUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TimerState())
    val uiState: StateFlow<TimerState> = _uiState.asStateFlow()

    private val _sideEffects = Channel<TimerSideEffect>()
    val sideEffects: Flow<TimerSideEffect> = _sideEffects.receiveAsFlow()


    fun startTimer(
        character: Character,
        level: Int,
    ) = viewModelScope.launch {
        val resIds = getAudioResIdUseCase(character, level, 0)
        _uiState.update { it.copy(text = "타이머 울리는 중") }
        _sideEffects.send(TimerSideEffect.PlayAudio(resIds))
    }
}
