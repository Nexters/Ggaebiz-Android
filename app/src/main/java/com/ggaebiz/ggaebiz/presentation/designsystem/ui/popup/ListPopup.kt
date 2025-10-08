package com.ggaebiz.ggaebiz.presentation.designsystem.ui.popup

import GaeBizBasePopup
import GaeBizPopupButton
import GaeBizPopupPosition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.R
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

@Preview(name = "ListPopup", showBackground = true, apiLevel = 34)
@Composable
fun ListPopupPreview() {
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
            ){
                ListPopup(
                    visible = true,
                    titleText = "이미지 선택",
                    subtitleText = "가로로 3개 샘플",
                    itemContent = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            val samples = listOf(
                                R.drawable.ic_selected_kiki_level1,
                                R.drawable.ic_selected_kiki_level2,
                                R.drawable.ic_selected_kiki_level3
                            )
                            samples.forEach { resId ->
                                Image(
                                    painter = painterResource(id = resId),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    },
                    position = GaeBizPopupPosition.Bottom,
                )
            }
        }
    }
}
