package com.ggaebiz.ggaebiz.presentation

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.designsystem.component.GaeBizTextAppBar
import com.ggaebiz.ggaebiz.presentation.designsystem.component.PickerState
import com.ggaebiz.ggaebiz.presentation.designsystem.component.rememberPickerState
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import com.ggaebiz.ggaebiz.presentation.ui.GaeBizLevelSlider
import com.ggaebiz.ggaebiz.presentation.ui.GaeBizPointText
import com.ggaebiz.ggaebiz.presentation.ui.GaeBizSlideButton
import com.ggaebiz.ggaebiz.presentation.ui.GaeBizSpeechText
import com.ggaebiz.ggaebiz.presentation.ui.GaeBizTimePicker

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GaeBizTheme {
                Greeting(
                    name = "Android",
                    modifier = Modifier.padding(),
                    context = this,
                )
            }
        }
    }
}

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier,
    context: Context? = null,
) {
    var selectedLevel by remember { mutableIntStateOf(1) }
    val hourPickerState: PickerState = rememberPickerState()
    val minutePickerState: PickerState = rememberPickerState()
    
    Column {
//        GaeBizLogoAppBar(
//            modifier = Modifier,
//            logoDrawable = R.drawable.ggaebiz_kor,
//        )
        GaeBizTextAppBar(
            titleRes = R.string.setting_title,
            iconOnClick = {},
        )

        Spacer(modifier = Modifier.height(8.dp))
        GaeBizLevelSlider(
            selectedLevel = selectedLevel,
            onValueChange = { newValue ->
                selectedLevel = newValue
                Toast.makeText(context, "현재 slider = ${newValue}", LENGTH_SHORT).show()
            },
        )
        
//        GaeBizTimePicker(
//            initHour = 5,
//            initMinute = 30,
//            onTimeChange = { hour, minute ->
//                Toast.makeText(context, "$hour : $minute", LENGTH_SHORT).show()
//            },
//        )
        
        Spacer(modifier = Modifier.height(8.dp))
        GaeBizSpeechText(
            text = "키키랑 타이머 설정하기",
            textStyle = GaeBizTheme.typography.bodySemiBold,
        )

        Spacer(modifier = Modifier.height(8.dp))
        GaeBizPointText(
            text = "까불이",
            textStyle = GaeBizTheme.typography.bodySemiBold,
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        GaeBizTimePicker(
            hourPickerState = hourPickerState,
            minutePickerState = minutePickerState,
            onScrollFinished = {
                Toast.makeText(context, "${hourPickerState.selectedItem} : ${minutePickerState.selectedItem}", LENGTH_SHORT).show()
            }
        )

        Spacer(modifier = Modifier.height(8.dp))
        GaeBizSlideButton(
            modifier = Modifier.width(280.dp),
            text = "타이머 끝내기",
            onSlideComplete = {
                Toast.makeText(context, "잠금 해제!", LENGTH_SHORT).show()
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GaeBizTheme {
        Greeting("Android")
    }
}
