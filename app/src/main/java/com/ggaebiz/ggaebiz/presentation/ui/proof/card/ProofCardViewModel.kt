package com.ggaebiz.ggaebiz.presentation.ui.proof.card

import androidx.lifecycle.SavedStateHandle
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.domain.usecase.CreateCachedImageUseCase
import com.ggaebiz.ggaebiz.domain.usecase.GetCharacterIdxUseCase
import com.ggaebiz.ggaebiz.domain.usecase.GetCurrentTimerUseCase
import com.ggaebiz.ggaebiz.domain.usecase.GetSettingTimerUseCase
import com.ggaebiz.ggaebiz.domain.usecase.SaveImageToGalleryUseCase
import com.ggaebiz.ggaebiz.presentation.common.base.BaseViewModel
import com.ggaebiz.ggaebiz.presentation.model.Character.Companion.CHARACTER_LIST
import com.ggaebiz.ggaebiz.presentation.ui.setting.ConcentrateType
import com.ggaebiz.ggaebiz.presentation.ui.setting.TimerMode

class ProofCardViewModel(
    private val createProofCardUseCase: CreateCachedImageUseCase,
    private val saveImageToGalleryUseCase: SaveImageToGalleryUseCase,
    private val getCharacterIdxUseCase: GetCharacterIdxUseCase,
    private val getCurrentTimerUseCase: GetCurrentTimerUseCase,
    private val getSettingTimerUseCase: GetSettingTimerUseCase,
) : BaseViewModel<ProofCardState, ProofCardIntent, ProofCardSideEffect>(
    ProofCardState()
) {

    fun processIntent(intent: ProofCardIntent) {
        when (intent) {
            is ProofCardIntent.EnterScreen -> {
                loadProofCardData()
            }

            is ProofCardIntent.CreateProofCardBitmap -> {
                updateState { it.copy(previewBitmap = intent.bitmap) }
            }

            ProofCardIntent.ClickSaveImage -> {
                updateState { it.copy(isSaveMode = true) }
            }

            ProofCardIntent.ClickShareImage -> {
                createAndShareImage()
            }

            ProofCardIntent.ClickBack -> {
                updateState { it.copy(isSaveMode = false) }
            }

            ProofCardIntent.LongPressImage -> {
                if (uiState.value.isSaveMode){
                    createAndSaveImage()
                }
            }
        }
    }

    private fun loadProofCardData() = launch {
        val characterIdx = getCharacterIdxUseCase()
        val (_, _, _, timerMode) = getCurrentTimerUseCase()
        val (hour, minute) = getSettingTimerUseCase()

        val concentrateType = when (timerMode) {
            is TimerMode.Concentrate -> timerMode.type
            else -> ConcentrateType.NORMAL
        }
        val characterImageResId = getCharacterImageResId(
            characterIdx = characterIdx,
            concentrateType = concentrateType
        )
        updateState {
            it.copy(
                hour = hour,
                minute = minute,
                characterImageResId = characterImageResId
            )
        }
    }

    private fun createAndSaveImage() = launch {
        val uri = uiState.value.previewBitmap?.let {
            createProofCardUseCase(it)
        }
        uri?.let {
            saveImageToGalleryUseCase(it)
                .onSuccess {
                    postSideEffect(ProofCardSideEffect.SaveImageSuccess)
                }
                .onFailure {
                    postSideEffect(ProofCardSideEffect.SaveImageFailure(R.string.proof_card_save_failure_text))
                }
        } ?: run {
            postSideEffect(ProofCardSideEffect.SaveImageFailure(R.string.proof_card_create_failure_text))
        }
    }

    private fun createAndShareImage() = launch {
        val uri = uiState.value.previewBitmap?.let {
            createProofCardUseCase(it)
        }
        uri?.let {
            postSideEffect(ProofCardSideEffect.ShareImage(it))
        } ?: run {
            postSideEffect(ProofCardSideEffect.ShowToast(R.string.proof_card_create_failure_text))
        }
    }

    private fun getCharacterImageResId(
        characterIdx: Int,
        concentrateType: ConcentrateType
    ): Int {
        val character = CHARACTER_LIST.getOrNull(characterIdx) ?: CHARACTER_LIST[0]
        
        return when (concentrateType) {
            ConcentrateType.NORMAL -> character.proofCardNormalImageResId
            ConcentrateType.STUDY -> character.proofCardStudyImageResId
            ConcentrateType.EXERCISE -> character.proofCardExerciseImageResId
        }
    }
}

