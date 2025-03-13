package com.ggaebiz.ggaebiz.presentation.ui.config

import com.ggaebiz.ggaebiz.domain.repository.ConfigRepository
import com.ggaebiz.ggaebiz.presentation.common.base.BaseViewModel


data class ConfigState(
    val isVibration: Boolean = false,
    val vibrationValue: Int = 3,
    val volumeValue: Int = 3,
) {
    val isVibrationSlider = isVibration
}

sealed interface ConfigIntent {
    data object EnterScreen : ConfigIntent
    data object ClickBack : ConfigIntent
    data object ChangeSwitch : ConfigIntent
    data class ChangeVibrationValue(val value: Int) : ConfigIntent
    data class ChangeVolumeValue(val value: Int) : ConfigIntent
    data object ClickCleatButton : ConfigIntent
}

sealed interface ConfigSideEffect {
    data object NavigateBack : ConfigSideEffect
    data object MoveToDeviceSetting : ConfigSideEffect
}


class ConfigViewModel(
    private val configRepository: ConfigRepository,
) : BaseViewModel<ConfigState, ConfigIntent, ConfigSideEffect>(ConfigState()) {
    fun processIntent(intent: ConfigIntent) {
        when (intent) {
            ConfigIntent.EnterScreen -> {
                launch {
                    val vibrationStatus = configRepository.getVibrationStatus()
                    val vibration = configRepository.getVibrationValue()
                    val volume = configRepository.getVolumeValue()

                    updateState {
                        it.copy(
                            isVibration = vibrationStatus,
                            vibrationValue = vibration,
                            volumeValue = volume
                        )
                    }
                }
            }

            ConfigIntent.ClickBack -> postSideEffect(ConfigSideEffect.NavigateBack)
            ConfigIntent.ChangeSwitch -> {
                launch {
                    val status = configRepository.getVibrationStatus()
                    configRepository.setVibrationStatus(!status)
                    updateState { it.copy(isVibration = !status) }
                }
            }

            is ConfigIntent.ChangeVibrationValue -> {
                launch { configRepository.setVibrationValue(intent.value) }
                updateState { it.copy(vibrationValue = intent.value) }
            }

            is ConfigIntent.ChangeVolumeValue -> {
                launch { configRepository.setVolumeValue(intent.value) }
                updateState { it.copy(volumeValue = intent.value) }
            }

            ConfigIntent.ClickCleatButton -> postSideEffect(ConfigSideEffect.MoveToDeviceSetting)
        }
    }
}
