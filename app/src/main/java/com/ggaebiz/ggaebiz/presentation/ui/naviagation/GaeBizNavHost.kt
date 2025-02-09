package com.ggaebiz.ggaebiz.presentation.ui.naviagation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ggaebiz.ggaebiz.presentation.ui.alarm.AlarmScreen
import com.ggaebiz.ggaebiz.presentation.ui.home.HomeScreen
import com.ggaebiz.ggaebiz.presentation.ui.setting.SettingScreen
import com.ggaebiz.ggaebiz.presentation.ui.splash.SplashScreen
import com.ggaebiz.ggaebiz.presentation.ui.timer.TimerScreen

@Composable
fun GaeBizNavHost(
    navigator: NavigatorState,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navigator.navController,
        startDestination = navigator.startDestination,
        modifier = modifier,
    ) {
        composable<Route.Splash> {
            SplashScreen(
                navigateHome = { navigator.navigateHome() }
            )
        }
        composable<Route.Home> {
            HomeScreen(
                navigateSetting = { selectedCharacterIndex ->
                    navigator.navigateSetting(selectedCharacterIndex)
                }
            )
        }
        composable<Route.Setting> {
            SettingScreen()
        }
        composable<Route.Timer> {
            TimerScreen()
        }
        composable<Route.Alarm> {
            AlarmScreen()
        }
    }
}
