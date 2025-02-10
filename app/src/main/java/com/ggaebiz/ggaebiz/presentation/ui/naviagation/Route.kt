package com.ggaebiz.ggaebiz.presentation.ui.naviagation

import kotlinx.serialization.Serializable

sealed interface Route  {
    @Serializable data object Splash : Route

    @Serializable data object Home : Route

    @Serializable data object Setting : Route

    @Serializable data class Timer(val selectedCharacterIndex: Int, val hour: Int, val minute: Int, val level: Int) : Route

    @Serializable data object Alarm : Route

    fun Route.toNavRoute(): String {
        return when (this) {
            is Splash -> "Splash"
            is Home -> "Home"
            is Setting -> "Setting/$selectedCharacterIndex"
            is Timer -> "Timer/$selectedCharacterIndex/$hour/$minute/$level"
            is Alarm -> "Alarm"
        }
    }
}
