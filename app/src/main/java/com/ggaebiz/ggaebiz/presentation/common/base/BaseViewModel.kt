package com.ggaebiz.ggaebiz.presentation.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<S : Any, I : Any, E : Any>(
    initialState: S,
) : ViewModel() {
    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<S> = _uiState.asStateFlow()

    private val _sideEffects = Channel<E>()
    val sideEffects: Flow<E> = _sideEffects.receiveAsFlow()

    protected fun updateState(reducer: (S) -> S) {
        _uiState.update(reducer)
    }

    protected fun postSideEffect(effect: E) {
        launch { _sideEffects.send(effect) }
    }

    protected fun launch(
        onError: (Throwable) -> Unit = { it.printStackTrace() },
        block: suspend CoroutineScope.() -> Unit,
    ) {
        viewModelScope.launch {
            try {
                block()
            } catch (e: Throwable) {
                onError(e)
            }
        }
    }
}
