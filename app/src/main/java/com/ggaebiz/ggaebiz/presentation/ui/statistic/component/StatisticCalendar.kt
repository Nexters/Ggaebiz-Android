package com.ggaebiz.ggaebiz.presentation.ui.statistic.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import com.ggaebiz.ggaebiz.presentation.ui.statistic.StatisticCalendarState
import com.ggaebiz.ggaebiz.presentation.ui.statistic.StatisticDateUiModel
import com.ggaebiz.ggaebiz.presentation.ui.statistic.StatisticLevel

@Composable
fun StatisticCalendar(
    state: StatisticCalendarState,
    onClickDate: (StatisticDateUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            state.dayOfWeeks.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = GaeBizTheme.typography.label3,
                    color = GaeBizTheme.colors.gray400,
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        state.dates.chunked(7).forEach { week ->
            Row(modifier = Modifier.fillMaxWidth()) {
                week.forEach { date ->
                    StatisticDateCell(
                        modifier = Modifier.weight(1f),
                        state = date,
                        onClick = { onClickDate(date) },
                    )
                }
                repeat(7 - week.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun StatisticDateCell(
    state: StatisticDateUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = when {
        state.isSelected -> GaeBizTheme.colors.primaryOrange
        state.level == StatisticLevel.High -> GaeBizTheme.colors.primaryOrange600
        state.level == StatisticLevel.Medium -> GaeBizTheme.colors.primaryOrange
        state.level == StatisticLevel.Low -> GaeBizTheme.colors.primaryOrange100
        else -> Color.Transparent
    }
    val textColor = when {
        state.isSelected || state.level == StatisticLevel.High || state.level == StatisticLevel.Medium -> GaeBizTheme.colors.white
        !state.isCurrentMonth -> GaeBizTheme.colors.gray200
        else -> GaeBizTheme.colors.gray800
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(3.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(enabled = state.isCurrentMonth, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = state.day.toString(),
            color = textColor,
            style = GaeBizTheme.typography.label3,
        )
    }
}