package com.ggaebiz.ggaebiz.presentation.ui.statistic.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import com.ggaebiz.ggaebiz.presentation.ui.statistic.StatisticCalendarState
import com.ggaebiz.ggaebiz.presentation.ui.statistic.StatisticDateUiModel
import com.ggaebiz.ggaebiz.presentation.ui.statistic.StatisticLevel

private val DOT_SIZE = 32.dp
private val TODAY_RING_SIZE = 36.dp
private val FEVER_SIZE = 20.dp
private val ROW_HEIGHT = 48.dp

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
        !state.isCurrentMonth -> Color.Transparent
        state.isFuture -> Color.Transparent
        state.level == StatisticLevel.Medium -> GaeBizTheme.colors.primaryOrange
        state.level == StatisticLevel.Low -> GaeBizTheme.colors.yellow
        else -> GaeBizTheme.colors.gray50
    }
    val textColor = when {
        !state.isCurrentMonth -> GaeBizTheme.colors.gray200
        state.isFuture -> GaeBizTheme.colors.gray900
        state.level == StatisticLevel.Low || state.level == StatisticLevel.Medium -> GaeBizTheme.colors.white
        else -> GaeBizTheme.colors.gray900
    }
    val numberStyle = GaeBizTheme.typography.body2Medium.copy(letterSpacing = 0.5.sp)

    Box(
        modifier = modifier.height(ROW_HEIGHT),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(DOT_SIZE)
                .clip(CircleShape)
                .background(backgroundColor)
                .clickable(enabled = state.isCurrentMonth, onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = state.day.toString(),
                color = textColor,
                style = numberStyle,
            )
        }
        // 오늘: 32dp 셀 위에 36dp 테두리 링(겹쳐서), 집중/연속 상태여도 항상 표시
        if (state.isToday) {
            Box(
                modifier = Modifier
                    .size(TODAY_RING_SIZE)
                    .border(2.dp, GaeBizTheme.colors.gray900, CircleShape),
            )
        }
        if (state.isFeverDay && !state.isFuture) {
            Image(
                painter = painterResource(R.drawable.ic_fire),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = 13.dp, y = (-13).dp)
                    .size(FEVER_SIZE),
            )
        }
    }
}
