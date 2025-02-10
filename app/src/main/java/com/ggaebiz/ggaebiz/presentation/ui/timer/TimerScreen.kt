package com.ggaebiz.ggaebiz.presentation.ui.timer

import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.designsystem.component.header.GaeBizLogoAppBar
import com.ggaebiz.ggaebiz.presentation.designsystem.component.timer.GaeBizTimer
import com.ggaebiz.ggaebiz.presentation.designsystem.ui.GaeBizMent
import com.ggaebiz.ggaebiz.presentation.designsystem.ui.GaeBizSlideButton
import com.ggaebiz.ggaebiz.presentation.feature.Character.Companion.CHARACTER_LIST
import kotlinx.coroutines.delay

@Composable
fun TimerScreen(
    navigateHome: () -> Unit,
    navigateAlarm: () -> Unit,
    selectedCharacterIndex: Int,
    hour: Int,
    minute: Int,
    level: Int,
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val imageWidth = screenWidth / 3 * 2
    var remainingSeconds by remember { mutableIntStateOf(hour * 3600 + minute * 60) }

    if (remainingSeconds >= 3600) {
        Toast.makeText(context, stringResource(R.string.timer_time_minute_toast_text, stringResource(CHARACTER_LIST[selectedCharacterIndex].nameResId), hour, minute), LENGTH_SHORT).show()
    } else {
        Toast.makeText(context, stringResource(R.string.timer_minute_toast_text, stringResource(CHARACTER_LIST[selectedCharacterIndex].nameResId), minute), LENGTH_SHORT).show()
    }

    LaunchedEffect(remainingSeconds) {
        if (remainingSeconds > 0) {
            delay(1000L)
            remainingSeconds -= 1
        } else {
            navigateAlarm()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        GaeBizLogoAppBar()

        Spacer(modifier = Modifier.height(58.dp))
        GaeBizMent(
            text = stringResource(CHARACTER_LIST[selectedCharacterIndex].initMentResId),
        )
        
        Spacer(modifier = Modifier.height(30.dp))
        Image(
            painter = painterResource(CHARACTER_LIST[selectedCharacterIndex].imageResId[level - 1]),
            contentDescription = null,
            modifier = Modifier.size(imageWidth),
            contentScale = ContentScale.Crop,
        )
        
        GaeBizTimer(remainingSeconds = remainingSeconds)

        Spacer(modifier = Modifier.weight(1f))
        GaeBizSlideButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 58.dp, end = 58.dp, bottom = 96.dp),
            text = stringResource(R.string.end_button_text),
            onSlideComplete = navigateHome,
        )
    }
}
