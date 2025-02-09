package com.ggaebiz.ggaebiz.presentation.ui.splash

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SplashScreen(
    navigateHome: () -> Unit,
) {
    Button(
        onClick = navigateHome
    ) {
        Text("홈 이동")
    }
}