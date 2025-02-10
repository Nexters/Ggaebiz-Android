package com.ggaebiz.ggaebiz.presentation.designsystem.component.header

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.designsystem.component.button.GaeBizIconButton
import com.ggaebiz.ggaebiz.presentation.designsystem.component.icon.GaeBizIcon
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme

@Composable
fun GaeBizTextAppBar(
    modifier: Modifier = Modifier,
    @StringRes titleRes: Int,
    iconImageVector: ImageVector = GaeBizIcon.icBack,
    iconOnClick: () -> Unit = { },
) {
    Box(modifier = modifier.wrapContentSize()) {
        Box(
            modifier = modifier.wrapContentSize().padding(
                horizontal = 16.dp,
                vertical = 12.dp,
            ),
        ) {
            GaeBizIconButton(
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
                style = GaeBizTheme.typography.titleSemiBold,
                color = GaeBizTheme.colors.gray800,
            )
        }
    }
}

@Preview("Text App Bar")
@Composable
private fun GaeBizTextAppBarPreview() {
    GaeBizTextAppBar(
        titleRes = R.string.setting_title_text,
        iconOnClick = {},
    )
}
