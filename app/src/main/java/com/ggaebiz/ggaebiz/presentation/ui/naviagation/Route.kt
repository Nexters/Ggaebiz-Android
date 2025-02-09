package com.ggaebiz.ggaebiz.presentation.ui.naviagation

import kotlinx.serialization.Serializable

sealed interface Route  {
    @Serializable data object Splash : Route

    @Serializable data object Home : Route

    @Serializable data object Setting : Route

    @Serializable data object Timer : Route

    @Serializable data object Alarm : Route
}
