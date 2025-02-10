package com.ggaebiz.ggaebiz.presentation.designsystem.component.timer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.presentation.designsystem.component.icon.GaeBizIcon
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import java.util.Locale

@Composable
fun GaeBizTimer(
    modifier: Modifier = Modifier,
    remainingSeconds: Int,
) {
    val timeSet = remember(remainingSeconds) {
        val hour = remainingSeconds / 3600
        val minute = (remainingSeconds % 3600) / 60
        val second = remainingSeconds % 60

        if (hour > 0) {
            listOf(
                String.format(Locale.ROOT, "%02d", hour),
                String.format(Locale.ROOT, "%02d", minute),
                String.format(Locale.ROOT, "%02d", second),
            )
        } else {
            listOf(
                String.format(Locale.ROOT, "%02d", minute),
                String.format(Locale.ROOT, "%02d", second),
            )
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        timeSet.forEachIndexed { index, timeSetPart ->
            Text(
                text = timeSetPart,
                style = GaeBizTheme.typography.timer1,
                textAlign = TextAlign.Center
            )
            if (index < timeSet.size - 1) {
                TimerLargeColon()
            }
        }
    }
}

@Preview("Timer")
@Composable
private fun GaeBizTimerPreview() {
    val secondsRemaining by remember { mutableIntStateOf(3600) }
    GaeBizTimer(remainingSeconds = secondsRemaining)
}
