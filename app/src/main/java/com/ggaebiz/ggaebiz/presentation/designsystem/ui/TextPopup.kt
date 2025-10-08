package com.ggaebiz.ggaebiz.presentation.designsystem.ui

import GaeBizBasePopup
import GaeBizPopupButton
import GaeBizPopupPosition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import androidx.compose.foundation.layout.Box as Box

@Composable
fun TextPopup(
    visible: Boolean,
    onDismissRequest: () -> Unit = {},
    titleText: String,
    bodyText: String,
    modifier: Modifier = Modifier,
    position: GaeBizPopupPosition = GaeBizPopupPosition.Center,
    buttons: List<GaeBizPopupButton>,
) {
    GaeBizBasePopup(
        visible = visible,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        position = position,
        title = {
            Text(
                text = titleText,
                color = GaeBizTheme.colors.gray900,
                style = GaeBizTheme.typography.titleSemiBold
            )
        },
        subtitle = null,
        content = {
            Text(
                text = bodyText,
                color = GaeBizTheme.colors.gray600,
                style = GaeBizTheme.typography.bodyMedium
            )
        },
        buttons = buttons
    )
}


@Preview(name = "TextPopup Samples", showBackground = true)
@Composable
fun GeaBizTextPopupPreview() {
    GaeBizTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .background(GaeBizTheme.colors.gray50),
                contentAlignment = Alignment.Center
            ) {
                TextPopup(
                    visible = true,
                    onDismissRequest = { },
                    titleText = "샘플 타이틀",
                    bodyText = "샘플 본문입니다. 알림을 더욱 정확하게 받기 위해 설정을 확인해 주세요.",
                    position = GaeBizPopupPosition.Center,
                    buttons = listOf(
                        GaeBizPopupButton(
                            text = "확인",
                            style = GaeBizButtonStyle.Primary,
                            onClick = { /* no-op */ }
                        )
                    )
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .background(GaeBizTheme.colors.gray50),
                contentAlignment = Alignment.Center
            ) {
                TextPopup(
                    visible = true,
                    onDismissRequest = {},
                    titleText = "업데이트 안내",
                    bodyText = "새로운 기능이 추가되었습니다. 지금 바로 확인해 보세요!",
                    position = GaeBizPopupPosition.Bottom,
                    buttons = listOf(
                        GaeBizPopupButton(
                            text = "나중에",
                            style = GaeBizButtonStyle.Secondary,
                            onClick = { }
                        ),
                        GaeBizPopupButton(
                            text = "자세히",
                            style = GaeBizButtonStyle.Primary,
                            onClick = { /* no-op */ }
                        )
                    )
                )
            }
        }
    }
}
