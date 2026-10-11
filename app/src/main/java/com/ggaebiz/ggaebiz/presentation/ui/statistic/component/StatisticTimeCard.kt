package com.ggaebiz.ggaebiz.presentation.ui.statistic.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import com.ggaebiz.ggaebiz.presentation.ui.statistic.StatisticModeBreakdownState
import com.ggaebiz.ggaebiz.presentation.ui.statistic.StatisticPeriod
import com.ggaebiz.ggaebiz.presentation.ui.statistic.StatisticTimeCardState

@Composable
fun StatisticTimeCard(
    state: StatisticTimeCardState,
    onSelectPeriod: (StatisticPeriod) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(GaeBizTheme.colors.white, RoundedCornerShape(24.dp))
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(40.dp),
    ) {
        StatisticTimeCardHeader(
            title = state.title,
            periodLabel = state.periodLabel,
            selectedPeriod = state.selectedPeriod,
            onSelectPeriod = onSelectPeriod,
        )
        StatisticTimeCardBody(
            hour = state.hour,
            minute = state.minute,
            second = state.second,
            breakdown = state.breakdown,
        )
    }
}

@Composable
private fun StatisticTimeCardHeader(
    title: String,
    periodLabel: String,
    selectedPeriod: StatisticPeriod,
    onSelectPeriod: (StatisticPeriod) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = title,
                style = GaeBizTheme.typography.titleSemiBold,
                color = GaeBizTheme.colors.gray900,
            )
            Text(
                text = periodLabel,
                style = GaeBizTheme.typography.label3,
                color = GaeBizTheme.colors.gray400,
            )
        }
        StatisticPeriodTabs(
            selected = selectedPeriod,
            onSelect = onSelectPeriod,
        )
    }
}

@Composable
private fun StatisticTimeCardBody(
    hour: String,
    minute: String,
    second: String,
    breakdown: List<StatisticModeBreakdownState>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        StatisticTimeBigRow(hour = hour, minute = minute, second = second)
        if (breakdown.isNotEmpty()) {
            HorizontalDivider(color = GaeBizTheme.colors.gray50)
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                breakdown.forEach { item ->
                    StatisticModeBreakdownRow(state = item)
                }
            }
        }
    }
}

@Composable
private fun StatisticTimeBigRow(
    hour: String,
    minute: String,
    second: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
    ) {
        StatisticTimeUnit(value = hour, unit = "시간")
        StatisticTimeUnit(value = minute, unit = "분")
        StatisticTimeUnit(value = second, unit = "초")
    }
}

@Composable
private fun StatisticTimeUnit(
    value: String,
    unit: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = value,
            style = GaeBizTheme.typography.timer3,
            color = GaeBizTheme.colors.gray900,
        )
        Text(
            text = unit,
            style = GaeBizTheme.typography.titleSemiBold,
            color = GaeBizTheme.colors.gray700,
            modifier = Modifier.padding(bottom = 6.dp),
        )
    }
}

@Composable
private fun StatisticModeBreakdownRow(
    state: StatisticModeBreakdownState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (state.iconRes != null) {
                Image(
                    painter = painterResource(state.iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
            } else {
                Spacer(modifier = Modifier.size(20.dp))
            }
            Text(
                text = state.label,
                style = GaeBizTheme.typography.body2Medium,
                color = GaeBizTheme.colors.gray900,
            )
        }
        Text(
            text = state.timeText,
            style = GaeBizTheme.typography.body2Medium,
            color = GaeBizTheme.colors.gray400,
        )
    }
}
