package com.ggaebiz.ggaebiz.presentation.ui.statistic.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import com.ggaebiz.ggaebiz.presentation.ui.statistic.StatisticCharacterRankItemState
import com.ggaebiz.ggaebiz.presentation.ui.statistic.StatisticCharacterRankState

@Composable
fun StatisticCharacterRankCard(
    state: StatisticCharacterRankState,
    onClickStartTimer: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(GaeBizTheme.colors.white, RoundedCornerShape(24.dp))
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        StatisticCharacterRankHeader(
            title = state.title,
            periodLabel = state.periodLabel,
            description = state.description,
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            state.items.forEach { item ->
                StatisticCharacterRankRow(state = item)
            }
        }
        StatisticStartTimerButton(
            characterName = state.ctaCharacterName,
            onClick = onClickStartTimer,
        )
    }
}

@Composable
private fun StatisticCharacterRankHeader(
    title: String,
    periodLabel: String,
    description: String,
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
        HorizontalDivider(color = GaeBizTheme.colors.gray50)
        Text(
            text = description,
            style = GaeBizTheme.typography.label3,
            color = GaeBizTheme.colors.gray400,
        )
    }
}

@Composable
private fun StatisticCharacterRankRow(
    state: StatisticCharacterRankItemState,
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
            Text(
                text = state.rank.toString(),
                style = GaeBizTheme.typography.bodyMedium,
                color = GaeBizTheme.colors.primaryOrange,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(12.dp),
            )
            Image(
                painter = painterResource(state.iconRes),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
            )
            Text(
                text = state.name,
                style = GaeBizTheme.typography.body2Medium,
                color = GaeBizTheme.colors.gray900,
            )
        }
        Text(
            text = state.countText,
            style = GaeBizTheme.typography.body2Medium,
            color = GaeBizTheme.colors.gray400,
        )
    }
}

@Composable
private fun StatisticStartTimerButton(
    characterName: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(GaeBizTheme.colors.primaryOrange)
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "${characterName}랑 타이머 시작",
            style = GaeBizTheme.typography.body2SemiBold,
            color = GaeBizTheme.colors.white,
        )
    }
}
