package com.ggaebiz.ggaebiz.presentation.designsystem.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.designsystem.component.chip.CategoryChip
import com.ggaebiz.ggaebiz.presentation.designsystem.component.icon.GaeBizIcon
import com.ggaebiz.ggaebiz.presentation.ui.setting.ConcentrateType
import com.ggaebiz.ggaebiz.presentation.ui.setting.TimerMode

@Composable
fun CategoryArea(
    modifier: Modifier,
    selected: TimerMode.Concentrate,
    onSelect: (ConcentrateType) -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CategoryChip(
            text = stringResource(R.string.normal_mode_text),
            selected = selected.type == ConcentrateType.NORMAL,
            onClick = { onSelect(ConcentrateType.NORMAL) }
        )
        CategoryChip(
            text = stringResource(R.string.study_mode_text),
            leadingIcon = GaeBizIcon.icPencil,
            selected = selected.type == ConcentrateType.STUDY,
            onClick = { onSelect(ConcentrateType.STUDY) }
        )
        CategoryChip(
            text = stringResource(R.string.exercise_mode_text),
            leadingIcon = GaeBizIcon.icBasketBall,
            selected = selected.type == ConcentrateType.EXERCISE,
            onClick = { onSelect(ConcentrateType.EXERCISE) }
        )
    }
}
