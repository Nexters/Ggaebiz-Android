package com.ggaebiz.ggaebiz.presentation.ui.naviagation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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
        composable(
            route = "Setting/{selectedCharacterIndex}",
            arguments = listOf(navArgument("selectedCharacterIndex") { type = NavType.IntType }),
        ) { backStackEntry ->
            val selectedCharacterIndex = backStackEntry.arguments?.getInt("selectedCharacterIndex") ?: 0
            SettingScreen(
                selectedCharacterIndex = selectedCharacterIndex,
                navigator = navigator,
                navigateTimer = { hour, minute, level ->
                    navigator.navigateTimer(
                        selectedCharacterIndex,
                        hour,
                        minute,
                        level,
                    )
                }
            )
        }
        composable(
            route = "Timer/{selectedCharacterIndex}/{hour}/{minute}/{level}",
            arguments = listOf(
                navArgument("selectedCharacterIndex") { type = NavType.IntType },
                navArgument("hour") { type = NavType.IntType },
                navArgument("minute") { type = NavType.IntType },
                navArgument("level") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val selectedCharacterIndex = backStackEntry.arguments?.getInt("selectedCharacterIndex") ?: 0
            val hour = backStackEntry.arguments?.getInt("hour") ?: 0
            val minute = backStackEntry.arguments?.getInt("minute") ?: 0
            val level = backStackEntry.arguments?.getInt("level") ?: 0
            TimerScreen(
                navigateHome = { navigator.navigateHome() },
                navigateAlarm = { navigator.navigateAlarm() },
                selectedCharacterIndex = selectedCharacterIndex,
                hour = hour,
                minute = minute,
                level = level,
            )
        }
        composable<Route.Alarm> {
            AlarmScreen()
        }
    }
}
