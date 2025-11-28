package com.ggaebiz.ggaebiz.presentation.designsystem.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.designsystem.component.icon.GaeBizIcon
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme

@Immutable
enum class TimerActionType { Pause, Resume }

@Composable
fun TimerActionButton(
    type: TimerActionType,
    visible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    if (!visible) return

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val background = when (type) {
        TimerActionType.Pause ->
            if (isPressed) GaeBizTheme.colors.gray50 else GaeBizTheme.colors.gray100

        TimerActionType.Resume ->
            if (isPressed) GaeBizTheme.colors.primaryOrange100 else GaeBizTheme.colors.primaryOrange50
    }
    val textColor = when (type) {
        TimerActionType.Pause -> GaeBizTheme.colors.black
        TimerActionType.Resume -> if (isPressed) GaeBizTheme.colors.primaryOrange600 else GaeBizTheme.colors.primaryOrange
    }

    val text = when (type) {
        TimerActionType.Pause -> stringResource(R.string.pause_text)
        TimerActionType.Resume -> stringResource(R.string.resume_text)
    }

    val icon = when (type) {
        TimerActionType.Pause -> GaeBizIcon.icPause
        TimerActionType.Resume -> GaeBizIcon.icResume
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(background)
            .defaultMinSize(minHeight = 38.dp)
            .padding(horizontal = 12.dp, vertical = 9.dp)
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 14.sp,
            style = GaeBizTheme.typography.body2SemiBold
        )

        Spacer(Modifier.width(2.dp))

        Image(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Preview("TimerActionButton")
@Composable
private fun TimerActionButtonPreview() {
    TimerActionButton(
        visible = true,
        type = TimerActionType.Resume,
        onClick = { },
    )
}
