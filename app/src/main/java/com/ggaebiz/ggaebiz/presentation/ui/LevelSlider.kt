package com.ggaebiz.ggaebiz.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.presentation.designsystem.component.GaeBizSlider
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme

@Composable
fun GaeBizLevelSlider(
    modifier: Modifier = Modifier,
    selectedLevel: Int,
    onValueChange: (Int) -> Unit,
    maxLevel: Int = 3,
    thumbSize: Dp = 20.dp,
    thumbColor: Color = GaeBizTheme.colors.gray900,
    trackHeight: Dp = 2.dp,
    activeTrackColor: Color = GaeBizTheme.colors.gray900,
    inactiveTrackColor: Color = GaeBizTheme.colors.gray75,
) {
    val levels = List(maxLevel) { it + 1 }

    Column(modifier = modifier.padding(16.dp)) {
        GaeBizSlider(
            modifier = Modifier.fillMaxWidth(),
            maxLevel = maxLevel,
            initialLevel = selectedLevel,
            onValueChange = onValueChange,
            thumbSize = thumbSize,
            thumbColor = thumbColor,
            trackHeight = trackHeight,
            activeTrackColor = activeTrackColor,
            inactiveTrackColor = inactiveTrackColor,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            levels.forEach { level ->
                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = "LV.$level",
                    color = if (level == selectedLevel) {
                        GaeBizTheme.colors.gray900
                    } else {
                        GaeBizTheme.colors.gray75
                    },
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Preview("Level Slider")
@Composable
private fun GaeBizLevelSliderPreview() {
    GaeBizLevelSlider(
        selectedLevel = 2,
        onValueChange = { },
    )
}
