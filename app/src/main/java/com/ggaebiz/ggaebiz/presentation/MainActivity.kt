package com.ggaebiz.ggaebiz.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.core.designsystem.component.GgaebizIconButton
import com.ggaebiz.ggaebiz.core.designsystem.component.GgaebizTopAppBar
import com.ggaebiz.ggaebiz.core.designsystem.icon.GgaebizIcon
import com.ggaebiz.ggaebiz.core.designsystem.theme.GgaebizTheme

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier,
) {
    Column {
        GgaebizTopAppBar(
            titleDrawable = R.drawable.ggaebiz_kor,
//            titleRes = R.string.setting_title,
            modifier = Modifier,
            navigationIcon = {
                GgaebizIconButton(
                    modifier = Modifier.size(48.dp),
                    onClick = {},
                    icon = {
                        Icon(
                            painter = GgaebizIcon.icBack(),
                            contentDescription = null,
                        )
                    },
                )
            },
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
