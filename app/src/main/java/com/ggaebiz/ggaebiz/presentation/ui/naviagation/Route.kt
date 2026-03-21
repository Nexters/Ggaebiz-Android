package com.ggaebiz.ggaebiz.presentation.ui.naviagation

import kotlinx.serialization.Serializable

sealed interface Route  {
    @Serializable data object Splash : Route

    @Serializable data object Login : Route

    @Serializable data object Onboarding : Route

    @Serializable data class Home(val isFromAlarm : Boolean?) : Route

    @Serializable data object Setting : Route

    @Serializable data object Timer : Route

    @Serializable data object Alarm : Route

    @Serializable data object Config : Route

    @Serializable data class Editor(val uri: String?) : Route

    @Serializable data class EditorResult(val uri: String) : Route
}
