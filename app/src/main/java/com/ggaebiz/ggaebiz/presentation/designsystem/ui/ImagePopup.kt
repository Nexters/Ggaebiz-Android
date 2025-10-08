package com.ggaebiz.ggaebiz.presentation.designsystem.ui

import GaeBizBasePopup
import GaeBizPopupButton
import GaeBizPopupPosition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme

@Composable
fun ImagePopup(
    visible: Boolean,
    onDismissRequest: () -> Unit = {},
    titleText: String,
    subtitleText: String? = null,
    image: Painter? = null,
    contentScale: ContentScale = ContentScale.Crop,
    position: GaeBizPopupPosition = GaeBizPopupPosition.Center,
    buttons: List<GaeBizPopupButton>,
) {
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
                Spacer(Modifier.height(4.dp))
                Text(
                    text = it,
                    color = GaeBizTheme.colors.gray600,
                    style = GaeBizTheme.typography.bodyMedium
                )
            }
        },
        content = {
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(GaeBizTheme.colors.white),
                contentAlignment = Alignment.Center
            ) {
                if (image != null) {
                    Image(
                        painter = image,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 160.dp),
                        contentScale = contentScale,
                    )
                } else {
                    Text(
                        text = "(image)",
                        color = GaeBizTheme.colors.gray400,
                        style = GaeBizTheme.typography.bodyMedium
                    )
                }
            }
        },
        buttons = buttons
    )
}

@Preview(name = "ImagePopup", showBackground = true)
@Composable
fun GaeBizImagePopupPreview() {
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
                    .background(GaeBizTheme.colors.gray50),
                contentAlignment = Alignment.Center
            ) {
                ImagePopup(
                    visible = true,
                    onDismissRequest = { /* no-op */ },
                    titleText = "샘플 이미지 팝업",
                    subtitleText = "샘플 서브타이틀",
                    image = painterResource( R.drawable.icon_battery),
                    position = GaeBizPopupPosition.Bottom,
                    buttons = listOf(
                        GaeBizPopupButton(
                            text = "확인",
                            style = GaeBizButtonStyle.Primary,
                            onClick = { /* no-op */ }
                        )
                    )
                )
            }
        }
    }
}
