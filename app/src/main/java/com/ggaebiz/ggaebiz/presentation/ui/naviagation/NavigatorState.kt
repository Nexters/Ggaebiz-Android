package com.ggaebiz.ggaebiz.presentation.ui.naviagation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberNavigator(
    navController: NavHostController = rememberNavController(),
): NavigatorState = remember(navController) {
    NavigatorState(
        navController = navController,
    )
}

class NavigatorState(
    val navController: NavHostController,
) {
    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val startDestination = Route.Splash

    fun navigateHome() {
        navController.navigate(Route.Home)
    }

    fun navigateOnboarding() {
        navController.navigate(Route.Onboarding)
    }

    fun navigateSetting() {
        navController.navigate(Route.Setting)
    }

    fun navigateTimer() {
        navController.navigate(Route.Timer)
    }

    fun navigateAlarm() {
        navController.navigate(Route.Alarm)
    }

    fun navigateConfig() {
        navController.navigate(Route.Config)
    }

    fun navigateEditor(uri: Uri?) {
        navController.navigate(Route.Editor(uri = uri?.toString()))
    }

    fun navigateEditorResult(uri: Uri) {
        navController.navigate(Route.EditorResult(uri = uri.toString()))
    }

    // 모든 화면을 제거하고 Home으로
    fun navigateToMainClearingBackStack() {
        navController.navigate(Route.Home) {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
        }
    }
    fun popBackStack() {
        navController.popBackStack()
    }
}
