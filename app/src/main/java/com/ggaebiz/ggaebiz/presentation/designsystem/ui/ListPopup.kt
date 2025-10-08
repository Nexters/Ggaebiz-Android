package com.ggaebiz.ggaebiz.presentation.designsystem.ui

import GaeBizBasePopup
import GaeBizPopupButton
import GaeBizPopupPosition
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme

@Composable
fun ListPopup(
    visible: Boolean,
    onDismissRequest: () -> Unit = {},
    titleText: String,
    subtitleText: String? = null,
    itemContent: @Composable () -> Unit,
    position: GaeBizPopupPosition = GaeBizPopupPosition.Center,
    buttons: List<GaeBizPopupButton> = emptyList(),
) {
    require(buttons.size <= 2) { "ListPopup supports up to 2 buttons." }

    GaeBizBasePopup(
        visible = visible,
        onDismissRequest = onDismissRequest,
        position = position,
        title = {
            Text(
                text = titleText,
                color = GaeBizTheme.colors.gray900,
                style = GaeBizTheme.typography.titleSemiBold
            )
        },
        subtitle = subtitleText?.let {
            {
                Text(
                    text = it,
                    color = GaeBizTheme.colors.gray600,
                    style = GaeBizTheme.typography.bodyMedium
                )
            }
        },
        content = { itemContent() },
        buttons = buttons
    )
}
