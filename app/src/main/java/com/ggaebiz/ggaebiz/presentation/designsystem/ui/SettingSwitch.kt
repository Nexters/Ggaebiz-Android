package com.ggaebiz.ggaebiz.presentation.designsystem.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.designsystem.component.textswitch.TextSwitchBox
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import com.ggaebiz.ggaebiz.presentation.ui.setting.ConcentrateType
import com.ggaebiz.ggaebiz.presentation.ui.setting.RestType
import com.ggaebiz.ggaebiz.presentation.ui.setting.TimerMode

@Composable
fun SettingSwitch(
    timerMode: TimerMode,
    onToggle: (TimerMode) -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = GaeBizTheme.colors.gray75,
    selectedBgColor: Color = GaeBizTheme.colors.white,
    unselectedBgColor: Color = GaeBizTheme.colors.gray75,
    selectedTextColor: Color = GaeBizTheme.colors.gray900,
    unselectedTextColor: Color = GaeBizTheme.colors.gray600,
    cornerRadius: Dp = 50.dp,
    height: Dp = 40.dp,
    selectedWeight: Float = 1.14f,
    unselectedWeight: Float = 0.86f,
) {
    val shape = RoundedCornerShape(cornerRadius)

    val restWeight by animateFloatAsState(
        targetValue = if (timerMode is TimerMode.Rest) selectedWeight else unselectedWeight,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "restWeight"
    )
    val concentrateWeight by animateFloatAsState(
        targetValue = if (timerMode is TimerMode.Rest) unselectedWeight else selectedWeight,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "concentrateWeight"
    )

    val restBackground by animateColorAsState(
        targetValue = if (timerMode is TimerMode.Rest) selectedBgColor else unselectedBgColor,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "restBackground"
    )
    val concentrateBackground by animateColorAsState(
        targetValue = if (timerMode is TimerMode.Concentrate) selectedBgColor else unselectedBgColor,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "concentrateBackground"
    )

    Row(
        modifier = modifier
            .clip(shape)
            .border(
                width = 2.dp,
                color = GaeBizTheme.colors.gray50,
                shape = RoundedCornerShape(cornerRadius)
            )
            .background(containerColor)
            .padding(vertical = 2.dp)
            .height(height)
            .width(132.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextSwitchBox(
            modifier = Modifier
                .padding(start = if (timerMode is TimerMode.Concentrate) 8.dp else 0.dp)
                .weight(restWeight)
                .fillMaxHeight()
                .clip(shape)
                .background(restBackground)
                .clickable {
                    if (timerMode is TimerMode.Concentrate) {
                        onToggle(TimerMode.Rest(RestType.NORMAL))
                    }
                },
            text = stringResource(R.string.rest_mode_text),
            isSelected = timerMode is TimerMode.Rest,
            selectedTextColor = selectedTextColor,
            unSelectedTextColor = unselectedTextColor,
            selectedTextStyle = GaeBizTheme.typography.body2Bold,
            unSelectedTextStyle = GaeBizTheme.typography.body2SemiBold,
        )

        Spacer(Modifier.width(4.dp))

        TextSwitchBox(
            modifier = Modifier
                .padding(end = if (timerMode is TimerMode.Rest) 8.dp else 0.dp)
                .weight(concentrateWeight)
                .fillMaxHeight()
                .clip(shape)
                .background(concentrateBackground)
                .clickable {
                    if (timerMode is TimerMode.Rest) {
                        onToggle(TimerMode.Concentrate(ConcentrateType.NORMAL))
                    }
                },
            text = stringResource(R.string.concentration_mode_text),
            isSelected = timerMode is TimerMode.Concentrate,
            selectedTextColor = selectedTextColor,
            unSelectedTextColor = unselectedTextColor,
            selectedTextStyle = GaeBizTheme.typography.body2Bold,
            unSelectedTextStyle = GaeBizTheme.typography.body2SemiBold,
        )
    }
}
