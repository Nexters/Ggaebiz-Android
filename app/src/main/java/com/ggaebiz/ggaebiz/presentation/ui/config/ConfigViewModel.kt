package com.ggaebiz.ggaebiz.presentation.ui.config

import androidx.annotation.StringRes
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.domain.repository.AuthRepository
import com.ggaebiz.ggaebiz.domain.repository.ConfigRepository
import com.ggaebiz.ggaebiz.domain.repository.NicknameRepository
import com.ggaebiz.ggaebiz.domain.repository.OnboardingRepository
import com.ggaebiz.ggaebiz.presentation.common.base.BaseViewModel


data class ConfigState(
    val nickname: String = "",
    val isVibration: Boolean = false,
    val vibrationValue: Int = 3,
    val volumeValue: Int = 3,
    val isNicknameSheetVisible: Boolean = false,
    val nicknameInput: String = "",
    val nicknameError: NicknameError = NicknameError.None,
    val isNicknameSaving: Boolean = false,
    val activeDialog: ConfigDialog = ConfigDialog.None,
    val isProcessing: Boolean = false,
    @StringRes val toastMessage: Int? = null,
) {
    val isVibrationSlider = isVibration
    val canSaveNickname: Boolean
        get() = nicknameInput.isNotBlank() &&
            nicknameError != NicknameError.Special &&
            !isNicknameSaving
}

enum class NicknameError {
    None,
    Length,
    Special,
}

enum class ConfigDialog {
    None,
    Logout,
    Withdraw,
}

sealed interface ConfigIntent {
    data object EnterScreen : ConfigIntent
    data object ClickBack : ConfigIntent
    data object ChangeSwitch : ConfigIntent
    data class ChangeVibrationValue(val value: Int) : ConfigIntent
    data class ChangeVolumeValue(val value: Int) : ConfigIntent
    data object ClickCleatButton : ConfigIntent
    data object ClickEditNickname : ConfigIntent
    data object DismissNicknameSheet : ConfigIntent
    data class ChangeNicknameInput(val value: String) : ConfigIntent
    data object ClickRandomNickname : ConfigIntent
    data object ClickSaveNickname : ConfigIntent
    data object ClearToast : ConfigIntent
    data object ClickLogout : ConfigIntent
    data object ClickWithdraw : ConfigIntent
    data object DismissDialog : ConfigIntent
    data object ConfirmLogout : ConfigIntent
    data object ConfirmWithdraw : ConfigIntent
}

sealed interface ConfigSideEffect {
    data object NavigateBack : ConfigSideEffect
    data object MoveToDeviceSetting : ConfigSideEffect
    data object NavigateToLogin : ConfigSideEffect
}


class ConfigViewModel(
    private val configRepository: ConfigRepository,
    private val nicknameRepository: NicknameRepository,
    private val authRepository: AuthRepository,
    private val onboardingRepository: OnboardingRepository,
) : BaseViewModel<ConfigState, ConfigIntent, ConfigSideEffect>(ConfigState()) {
    fun processIntent(intent: ConfigIntent) {
        when (intent) {
            ConfigIntent.EnterScreen -> {
                launch {
                    val vibrationStatus = configRepository.getVibrationStatus()
                    val vibration = configRepository.getVibrationValue()
                    val volume = configRepository.getVolumeValue()
                    val nickname = nicknameRepository.getNickname().orEmpty()

                    updateState {
                        it.copy(
                            nickname = nickname,
                            isVibration = vibrationStatus,
                            vibrationValue = vibration,
                            volumeValue = volume,
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

            ConfigIntent.ClickEditNickname -> updateState {
                it.copy(
                    isNicknameSheetVisible = true,
                    nicknameInput = it.nickname,
                    nicknameError = NicknameError.None,
                )
            }

            ConfigIntent.DismissNicknameSheet -> updateState {
                it.copy(isNicknameSheetVisible = false)
            }

            is ConfigIntent.ChangeNicknameInput -> changeNicknameInput(intent.value)

            ConfigIntent.ClickRandomNickname -> updateState {
                it.copy(
                    nicknameInput = nicknameRepository.generateRandomNickname(),
                    nicknameError = NicknameError.None,
                )
            }

            ConfigIntent.ClickSaveNickname -> saveNickname()

            ConfigIntent.ClearToast -> updateState { it.copy(toastMessage = null) }

            ConfigIntent.ClickLogout -> updateState { it.copy(activeDialog = ConfigDialog.Logout) }

            ConfigIntent.ClickWithdraw -> updateState { it.copy(activeDialog = ConfigDialog.Withdraw) }

            ConfigIntent.DismissDialog -> updateState { it.copy(activeDialog = ConfigDialog.None) }

            ConfigIntent.ConfirmLogout -> logout()

            ConfigIntent.ConfirmWithdraw -> withdraw()
        }
    }

    private fun changeNicknameInput(value: String) = updateState {
        when {
            !ALLOWED_NICKNAME_REGEX.matches(value) ->
                it.copy(nicknameInput = value, nicknameError = NicknameError.Special)

            value.length > MAX_NICKNAME_LENGTH ->
                it.copy(nicknameError = NicknameError.Length)

            else ->
                it.copy(nicknameInput = value, nicknameError = NicknameError.None)
        }
    }

    private fun saveNickname() {
        if (!uiState.value.canSaveNickname) return
        val nickname = uiState.value.nicknameInput.trim()

        launch {
            updateState { it.copy(isNicknameSaving = true) }
            nicknameRepository.updateNickname(nickname)
                .onSuccess {
                    updateState {
                        it.copy(
                            nickname = nickname,
                            isNicknameSheetVisible = false,
                            isNicknameSaving = false,
                            toastMessage = R.string.nickname_update_success,
                        )
                    }
                }
                .onFailure {
                    updateState {
                        it.copy(
                            isNicknameSheetVisible = false,
                            isNicknameSaving = false,
                            toastMessage = R.string.nickname_update_fail,
                        )
                    }
                }
        }
    }

    private fun logout() {
        if (uiState.value.isProcessing) return
        launch {
            updateState { it.copy(isProcessing = true) }
            authRepository.logout()
            updateState { it.copy(isProcessing = false, activeDialog = ConfigDialog.None) }
            postSideEffect(ConfigSideEffect.NavigateToLogin)
        }
    }

    private fun withdraw() {
        if (uiState.value.isProcessing) return
        launch {
            updateState { it.copy(isProcessing = true) }
            authRepository.withdraw()
                .onSuccess {
                    onboardingRepository.clearOnboarding()
                    updateState { it.copy(isProcessing = false, activeDialog = ConfigDialog.None) }
                    postSideEffect(ConfigSideEffect.NavigateToLogin)
                }
                .onFailure {
                    updateState {
                        it.copy(
                            isProcessing = false,
                            activeDialog = ConfigDialog.None,
                            toastMessage = R.string.withdraw_fail,
                        )
                    }
                }
        }
    }

    companion object {
        const val MAX_NICKNAME_LENGTH = 10
        private val ALLOWED_NICKNAME_REGEX = Regex("^[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9\\s]*$")
    }
}
