package com.ggaebiz.ggaebiz.presentation.designsystem.component.header

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
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
import com.ggaebiz.ggaebiz.presentation.designsystem.component.button.GaeBizButton
import com.ggaebiz.ggaebiz.presentation.designsystem.component.button.GaeBizIconButton
import com.ggaebiz.ggaebiz.presentation.designsystem.component.icon.GaeBizIcon
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme

@Composable
fun GaeBizTextAppBar(
    modifier: Modifier = Modifier,
    @StringRes titleRes: Int,
    iconImageVector: ImageVector = GaeBizIcon.icBack,
    iconOnClick: () -> Unit = { },
    rightContent: (@Composable () -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = 16.dp),
    ) {
        GaeBizIconButton(
            onClick = iconOnClick,
            iconImageVector = iconImageVector,
            modifier = Modifier.align(Alignment.CenterStart)
        )
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(id = titleRes),
            style = GaeBizTheme.typography.titleSemiBold,
            color = GaeBizTheme.colors.gray800,
        )
        if (rightContent != null) {
            Box(
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                rightContent()
            }
        }
    }
}

@Preview("Text App Bar", showBackground = true, apiLevel = 34)
@Composable
private fun GaeBizTextAppBarPreview() {
    GaeBizTextAppBar(
        titleRes = R.string.setting_title_text,
        iconOnClick = {},
        rightContent = {}
    )
}
