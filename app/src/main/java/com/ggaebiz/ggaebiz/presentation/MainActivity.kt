package com.ggaebiz.ggaebiz.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import com.ggaebiz.ggaebiz.presentation.ui.naviagation.GaeBizNavHost
import com.ggaebiz.ggaebiz.presentation.ui.naviagation.rememberNavigator

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navigator = rememberNavigator()
            GaeBizTheme {
                Scaffold { padding ->
                    GaeBizNavHost(navigator, modifier = Modifier.padding(padding))
                }
            }
        }
    }
}

