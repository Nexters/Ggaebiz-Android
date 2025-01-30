package com.ggaebiz.ggaebiz.presentation.designsystem.component

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.designsystem.icon.GgaebizIcon
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GgabizTypography
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.Gray800
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.White

@Composable
fun GgaebizTextAppBar(
    modifier: Modifier = Modifier,
    @StringRes titleRes: Int,
    color: Color = White,
    iconImageVector: ImageVector = GgaebizIcon.icBack,
    iconOnClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .background(color = color)
            .statusBarsPadding(),
    ) {
        Box(
            modifier = modifier.wrapContentSize().padding(
                horizontal = 16.dp,
                vertical = 12.dp,
            ),
        ) {
            GgaebizIconButton(
                onClick = iconOnClick,
                iconImageVector = iconImageVector,
            )
        }
        
        Box(
            modifier = modifier.fillMaxWidth().height(72.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(id = titleRes),
                style = GgabizTypography.titleLarge,
                color = Gray800,
            )
        }
    }
}

@Preview("Text App Bar")
@Composable
private fun GgaebizTextAppBarPreview() {
    GgaebizTextAppBar(
        titleRes = R.string.setting_title,
        iconOnClick = {},
    )
}
