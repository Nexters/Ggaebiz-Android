package com.ggaebiz.ggaebiz.presentation.designsystem.component.chip

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.designsystem.component.icon.GaeBizIcon
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme

@Composable
fun CategoryChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(15.dp),
    height: Dp = 40.dp,
    horizontalPadding: Dp = 10.dp,
    spacing: Dp = 4.dp,
    unselectedChipBackgroundColor: Color = GaeBizTheme.colors.gray50,
    selectedChipBackgroundColor: Color = GaeBizTheme.colors.gray900,
    unselectedTextColor: Color = GaeBizTheme.colors.gray900,
    selectedTextColor: Color = GaeBizTheme.colors.white,
    unselectedTextStyle: TextStyle = GaeBizTheme.typography.body2Medium,
    selectedTextStyle: TextStyle = GaeBizTheme.typography.body2SemiBold,
) {
    val backgroundColor by animateColorAsState(
        if (selected) selectedChipBackgroundColor else unselectedChipBackgroundColor,
        label = "backgroundColor"
    )
    val textColor by animateColorAsState(
        if (selected) selectedTextColor else unselectedTextColor,
        label = "textColor"
    )
    val textStyle = if (selected) selectedTextStyle else unselectedTextStyle

    Row(
        modifier = modifier
            .height(height)
            .clip(shape)
            .background(backgroundColor)
            .clickable(enabled = enabled) { onClick() }
            .padding(horizontal = horizontalPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (leadingIcon != null) {
            Image(
                imageVector = leadingIcon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
            )
        }

        Spacer(Modifier.width(spacing))

        Text(
            text = text,
            color = textColor,
            style = textStyle,
        )

        Spacer(Modifier.width(spacing))

        AnimatedVisibility(
            visible = selected,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut(),
        ) {
            if (selected) {
                Image(
                    imageVector = GaeBizIcon.icCheck,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    colorFilter = ColorFilter.tint(GaeBizTheme.colors.primaryOrange),
                )
            }
        }
    }
}

@Preview("Chip")
@Composable
private fun CategoryChipPreview() {
    CategoryChip(
        text = stringResource(R.string.normal_mode_text),
        selected = true,
        onClick = { }
    )
}
