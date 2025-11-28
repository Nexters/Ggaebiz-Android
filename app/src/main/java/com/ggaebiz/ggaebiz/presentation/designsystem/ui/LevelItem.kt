package com.ggaebiz.ggaebiz.presentation.designsystem.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme

@Composable
fun LevelItem(
    modifier: Modifier = Modifier,
    title: String,
    isSelected: Boolean,
    selectedIcon: Painter,
    unSelectedIcon: Painter,
    onClick: (() -> Unit),
    cornerRadius: Dp = 16.dp,
    selectedTextStyle: TextStyle = GaeBizTheme.typography.body2Bold,
    unSelectedTextStyle: TextStyle = GaeBizTheme.typography.body2Medium,
    selectedTextColor: Color = GaeBizTheme.colors.gray800,
    unSelectedTextColor: Color = GaeBizTheme.colors.gray300,
) {
    Column(
        modifier = modifier.wrapContentWidth(Alignment.CenterHorizontally),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
                .clip(RoundedCornerShape(cornerRadius))
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = if (isSelected) selectedIcon else unSelectedIcon,
                contentDescription = title,
                modifier = Modifier.fillMaxWidth(1f),
            )
        }

        Spacer(Modifier.height(6.dp))

        Text(
            text = title,
            style = if (isSelected) selectedTextStyle else unSelectedTextStyle,
            color = if (isSelected) selectedTextColor else unSelectedTextColor,
        )
    }
}
