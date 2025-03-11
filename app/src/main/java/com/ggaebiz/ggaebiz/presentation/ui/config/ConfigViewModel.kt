package com.ggaebiz.ggaebiz.presentation.ui.config

import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.common.base.BaseViewModel
import com.ggaebiz.ggaebiz.presentation.ui.alarm.AlarmIntent


data class ConfigState(
    val isVibration : Boolean = true
)

sealed interface ConfigIntent {
    data object ClickBack : ConfigIntent

}

sealed interface ConfigSideEffect {
    data object NavigateBack : ConfigSideEffect
}



class ConfigViewModel : BaseViewModel<ConfigState,ConfigIntent,ConfigSideEffect>(ConfigState()){

    fun processIntent(intent: ConfigIntent) {
        when (intent) {
            ConfigIntent.ClickBack -> postSideEffect(ConfigSideEffect.NavigateBack)
        }
    }

}