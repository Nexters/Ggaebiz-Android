package com.ggaebiz.ggaebiz.presentation.ui.statistic.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import com.ggaebiz.ggaebiz.presentation.ui.statistic.StatisticPeriod

@Composable
fun StatisticPeriodTabs(
    selected: StatisticPeriod,
    onSelect: (StatisticPeriod) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(GaeBizTheme.colors.gray25, RoundedCornerShape(14.dp))
            .padding(6.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        StatisticPeriod.values().forEach { period ->
            StatisticPeriodTab(
                modifier = Modifier.weight(1f),
                label = period.label(),
                isSelected = selected == period,
                onClick = { onSelect(period) },
            )
        }
    }
}

@Composable
private fun StatisticPeriodTab(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(32.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) GaeBizTheme.colors.white else Color.Transparent)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = if (isSelected) GaeBizTheme.typography.body2SemiBold
            else GaeBizTheme.typography.body2Medium,
            color = if (isSelected) GaeBizTheme.colors.primaryOrange
            else GaeBizTheme.colors.gray900,
        )
    }
}

private fun StatisticPeriod.label(): String = when (this) {
    StatisticPeriod.Month -> "월"
    StatisticPeriod.Week -> "주"
    StatisticPeriod.Day -> "일"
}
