package com.ggaebiz.ggaebiz.presentation.ui.naviagation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import com.ggaebiz.ggaebiz.presentation.ui.alarm.AlarmScreen
import com.ggaebiz.ggaebiz.presentation.ui.home.HomeScreen
import com.ggaebiz.ggaebiz.presentation.ui.setting.SettingScreen
import com.ggaebiz.ggaebiz.presentation.ui.splash.SplashScreen
import com.ggaebiz.ggaebiz.presentation.ui.timer.TimerScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun GaeBizNavHost(
    navigator: NavigatorState,
    modifier: Modifier = Modifier,
) {
    val defaultColor = GaeBizTheme.colors.gray25
    val navigationColor = GaeBizTheme.colors.black
    val primaryColor = GaeBizTheme.colors.primaryOrange
    val splashStatusBarColor = GaeBizTheme.colors.splashStatusBarColor

    val systemUiController = rememberSystemUiController()
    val currentDestination = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(navigator.navController) {
        navigator.navController.addOnDestinationChangedListener { _, destination, _ ->
            currentDestination.value = destination.route
        }
    }

    LaunchedEffect(currentDestination.value) {
        when (currentDestination.value) {
            Route.Splash::class.qualifiedName -> {
                systemUiController.setStatusBarColor(splashStatusBarColor, darkIcons = false)
                systemUiController.setNavigationBarColor(navigationColor)
            }
            Route.Alarm::class.qualifiedName -> {
                systemUiController.setStatusBarColor(primaryColor, darkIcons = false)
                systemUiController.setNavigationBarColor(navigationColor)
            }
            else -> {
                systemUiController.setStatusBarColor(defaultColor, darkIcons = true)
                systemUiController.setNavigationBarColor(defaultColor)
            }
        }
    }

    NavHost(
        navController = navigator.navController,
        startDestination = navigator.startDestination,
        modifier = modifier,
    ) {
        composable<Route.Splash> {
            SplashScreen(navigateHome = { navigator.navigateHome() }
            )
        }
        composable<Route.Home> {
            HomeScreen(
                navigateSetting = { navigator.navigateSetting() },
                navigateAlarm = { navigator.navigateAlarm() }
            )
        }
        composable<Route.Setting> {
            SettingScreen(
                navigatorHome = { navigator.navigateToMainClearingBackStack() },
                navigateTimer = { navigator.navigateTimer() },
            )
        }
        composable<Route.Timer> {
            TimerScreen(
                navigateHome = { navigator.navigateHome() }
            )
        }
        composable<Route.Alarm> {
            AlarmScreen(
                navigateStart = { navigator.navigateToMainClearingBackStack() },
                navigateTimer = { navigator.navigateTimer() }
            )
        }
    }
}
