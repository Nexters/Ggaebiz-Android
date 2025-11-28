package com.ggaebiz.ggaebiz.presentation.designsystem.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.designsystem.component.icon.GaeBizIcon
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme

@Composable
fun RestMent(
    modifier: Modifier = Modifier,
    text: String,
    level: Int,
    onClick: (() -> Unit),
    textStyle: TextStyle = GaeBizTheme.typography.bodySemiBold,
    levelTextStyle: TextStyle = GaeBizTheme.typography.label3,
    textColor: Color = GaeBizTheme.colors.gray800,
    levelTextColor: Color = GaeBizTheme.colors.primaryOrange,
    radius: Dp = 16.dp,
    levelRadius: Dp = 10.dp,
    backgroundColor: Color = GaeBizTheme.colors.white,
    pressedBackgroundColor: Color = GaeBizTheme.colors.gray50,
    levelBackgroundColor: Color = GaeBizTheme.colors.primaryOrange50,
) {
    val interaction = remember { MutableInteractionSource() }
    val isPressed by interaction.collectIsPressedAsState()

    val containerBackgroundColor by animateColorAsState(
        if (isPressed) pressedBackgroundColor else backgroundColor,
        label = "containerBackgroundColor"
    )

    Row(
        modifier = modifier
            .wrapContentSize()
            .background(
                color = containerBackgroundColor,
                shape = RoundedCornerShape(radius),
            )
            .padding(
                horizontal = 12.dp,
                vertical = 12.dp,
            )
            .clickable(
                interactionSource = interaction,
                indication = null,
                onClick = onClick,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = modifier
                .wrapContentSize()
                .background(
                    color = levelBackgroundColor,
                    shape = RoundedCornerShape(levelRadius),
                )
                .padding(
                    horizontal = 6.dp,
                    vertical = 8.dp,
                ),
        ) {
            Text(
                text = stringResource(R.string.ment_level_text, level),
                color = levelTextColor,
                style = levelTextStyle,
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            maxLines = 2,
            color = textColor,
            style = textStyle,
        )

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            imageVector = GaeBizIcon.icTopBottomArrow,
            contentDescription = null,
        )
    }
}
