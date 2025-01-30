package com.ggaebiz.ggaebiz.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.designsystem.component.GaeBizTextAppBar
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GgaebizTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GgaebizTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier,
) {
    Column {
        GaeBizTextAppBar(
//            titleDrawable = R.drawable.ggaebiz_kor,
            titleRes = R.string.setting_title,
            modifier = Modifier,
            iconOnClick = {},
        )
        Text(
            text = "Hello $name!",
            modifier = modifier,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GgaebizTheme {
        Greeting("Android")
    }
}
