package com.ggaebiz.ggaebiz.presentation.ui.alarm

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.designsystem.component.button.GaeBizButton
import com.ggaebiz.ggaebiz.presentation.designsystem.component.timer.TimerLargeColon
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme

@Composable
fun AlarmTopSection(
    ment: String,
    plusSecond: String,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(28.dp))
        AlarmDefaultText()
        Spacer(Modifier.height(4.dp))
        Text(
            text = plusSecond,
            color = GaeBizTheme.colors.red600,
            style = GaeBizTheme.typography.bodySemiBold
        )
        Spacer(Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(shape = RoundedCornerShape(16.dp))
                .background(GaeBizTheme.colors.black12)
                .padding(vertical = 15.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = ment,
                color = GaeBizTheme.colors.white,
                style = GaeBizTheme.typography.bodyBold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Stable
@Composable
private fun AlarmDefaultText() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(R.string.alarm_default_timer_text),
            style = GaeBizTheme.typography.timer1,
            color = GaeBizTheme.colors.white
        )
        TimerLargeColon(isBlack = false)
        Text(
            text = stringResource(R.string.alarm_default_timer_text),
            style = GaeBizTheme.typography.timer1,
            color = GaeBizTheme.colors.white
        )
    }
}


@Composable
fun AlarmBottomSection(
    modifier: Modifier = Modifier,
    onClickFinishButton: () -> Unit,
    onClickSnoozeButton: () -> Unit,
    isDisableSnoozeButton : Boolean
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        GaeBizButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.alarm_finish_timer_btn_text),
            style = GaeBizTheme.typography.bodySemiBold,
            containerColor = GaeBizTheme.colors.primaryOrange,
            contentColor = GaeBizTheme.colors.white,
            onClick = onClickFinishButton
        )
        Spacer(Modifier.height(12.dp))
        if (!isDisableSnoozeButton){
            Box(
                modifier = Modifier
                    .height(56.dp)
                    .clickable { onClickSnoozeButton() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.alarm_snooze_timer_btn_text),
                    style = GaeBizTheme.typography.bodyMedium,
                    color = GaeBizTheme.colors.gray100,
                )
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}