package com.ggaebiz.ggaebiz.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import com.ggaebiz.ggaebiz.presentation.service.TimerServiceManager
import com.ggaebiz.ggaebiz.presentation.ui.naviagation.GaeBizNavHost
import com.ggaebiz.ggaebiz.presentation.ui.naviagation.rememberNavigator
import org.koin.android.ext.android.inject
import org.koin.compose.getKoin

class MainActivity : ComponentActivity() {
    private val timerServiceManager: TimerServiceManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val shouldLaunchHome = intent?.getBooleanExtra("EXTRA_LAUNCH_HOME", false) ?: false
        setContent {
            val navigator = rememberNavigator()
            GaeBizTheme {
                Scaffold { padding ->
                    GaeBizNavHost(navigator, modifier = Modifier.padding(padding))
                }
                LaunchedEffect(Unit) {
                    if (shouldLaunchHome) {
                        navigator.navigateHome()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if ( timerServiceManager.isTimerServiceRunning(this)){
            timerServiceManager.stopTimerService()
        }
    }
}

