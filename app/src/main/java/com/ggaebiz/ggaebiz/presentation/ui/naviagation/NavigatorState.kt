package com.ggaebiz.ggaebiz.presentation.ui.naviagation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ggaebiz.ggaebiz.presentation.ui.naviagation.Route.Alarm.toNavRoute

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

    val startDestination = Route.Home

    fun navigateHome() {
        navController.navigate(Route.Home)
    }

    fun navigateSetting(selectedCharacterIndex: Int) {
        navController.navigate(Route.Setting(selectedCharacterIndex).toNavRoute())
    }

    fun navigateTimer(selectedCharacterIndex: Int, hour: Int, minute: Int, level: Int) {
        navController.navigate(Route.Timer(selectedCharacterIndex, hour, minute, level).toNavRoute())
    }

    fun navigateAlarm() {
        navController.navigate(Route.Alarm)
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
