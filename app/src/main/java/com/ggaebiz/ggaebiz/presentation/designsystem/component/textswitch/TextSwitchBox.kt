package com.ggaebiz.ggaebiz.presentation.designsystem.component.textswitch

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun TextSwitchBox(
    modifier: Modifier,
    text: String,
    isSelected: Boolean,
    selectedTextColor: Color,
    unSelectedTextColor: Color,
    selectedTextStyle: TextStyle,
    unSelectedTextStyle: TextStyle,
) {
    val textColor = if (isSelected) selectedTextColor else unSelectedTextColor
    val textStyle = if (isSelected) selectedTextStyle else unSelectedTextStyle
    
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = textColor,
            style = textStyle,
        )
    }
}
