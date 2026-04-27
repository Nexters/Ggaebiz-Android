package com.ggaebiz.ggaebiz.presentation.ui.statistic.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import com.ggaebiz.ggaebiz.presentation.ui.statistic.StatisticSummaryCardState

@Composable
fun StatisticSummaryCard(
    state: StatisticSummaryCardState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = GaeBizTheme.colors.primaryOrange,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(20.dp)
    ) {
        Text(
            text = state.title,
            style = GaeBizTheme.typography.titleSemiBold,
            color = GaeBizTheme.colors.white,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = state.description,
            style = GaeBizTheme.typography.body2Medium,
            color = GaeBizTheme.colors.white,
        )
    }
}