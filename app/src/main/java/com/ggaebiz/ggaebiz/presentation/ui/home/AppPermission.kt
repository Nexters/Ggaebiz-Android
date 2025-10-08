package com.ggaebiz.ggaebiz.presentation.ui.home

sealed interface AppPermission {
    data object Camera : AppPermission
    data object Gallery : AppPermission
    data object Notifications : AppPermission
}