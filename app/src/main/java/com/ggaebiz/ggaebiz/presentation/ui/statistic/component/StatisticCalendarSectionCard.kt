package com.ggaebiz.ggaebiz.presentation.ui.statistic.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import com.ggaebiz.ggaebiz.presentation.ui.statistic.StatisticCalendarState
import com.ggaebiz.ggaebiz.presentation.ui.statistic.StatisticDateUiModel

@Composable
fun StatisticCalendarSectionCard(
    state: StatisticCalendarState,
    onClickPreviousMonth: () -> Unit,
    onClickNextMonth: () -> Unit,
    onClickDate: (StatisticDateUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(GaeBizTheme.colors.white, RoundedCornerShape(24.dp))
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(40.dp),
    ) {
        Text(
            text = "집중 캘린더",
            style = GaeBizTheme.typography.titleSemiBold,
            color = GaeBizTheme.colors.gray900,
        )
        Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
            StatisticMonthHeader(
                yearMonthText = state.yearMonthText,
                onClickPreviousMonth = onClickPreviousMonth,
                onClickNextMonth = onClickNextMonth,
            )
            StatisticCalendar(
                state = state,
                onClickDate = onClickDate,
            )
            StatisticCalendarLegend()
        }
    }
}

@Composable
private fun StatisticCalendarLegend(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        StatisticCalendarLegendItem(label = "쉼", color = GaeBizTheme.colors.gray50)
        StatisticCalendarLegendItem(label = "집중", color = GaeBizTheme.colors.yellow)
        StatisticCalendarLegendItem(label = "연속 집중", color = GaeBizTheme.colors.primaryOrange)
        StatisticCalendarLegendItem(label = "최고 집중", iconRes = R.drawable.ic_fire)
    }
}

@Composable
private fun StatisticCalendarLegendItem(
    label: String,
    modifier: Modifier = Modifier,
    color: Color? = null,
    @DrawableRes iconRes: Int? = null,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        if (iconRes != null) {
            Image(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier.size(11.dp),
            )
        } else if (color != null) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color, CircleShape)
            )
        }
        Text(
            text = label,
            style = GaeBizTheme.typography.label4,
            color = GaeBizTheme.colors.gray900,
        )
    }
}
