package com.ggaebiz.ggaebiz.presentation.designsystem.component.header

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme

@Composable
fun GaeBizLogoRightIconAppBar(
    modifier: Modifier = Modifier,
    logoDrawable: Int = R.drawable.ggaebiz_kor,
    clickFirstRightIcon: () -> Unit,
    clickSecondRightIcon: () -> Unit,
    iconEnable : Boolean = true
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Image(
            modifier = modifier
                .height(64.dp)
                .padding(vertical = 22.dp),
            painter = painterResource(id = logoDrawable),
            contentDescription = stringResource(R.string.logo_img_description),
        )
        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .clickable(enabled = iconEnable) { clickSecondRightIcon() }
                    .background(GaeBizTheme.colors.gray50)
                    .padding(14.dp),
                painter = painterResource(id = R.drawable.ic_statistic),
                contentDescription = "Clickable Image"
            )
            Image(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .clickable(enabled = iconEnable) { clickFirstRightIcon() }
                    .background(GaeBizTheme.colors.gray50)
                    .padding(14.dp),
                painter = painterResource(id = R.drawable.icon_setting),
                contentDescription = "Clickable Image"
            )
        }
    }
}

@Preview("Logo App Bar")
@Composable
private fun GaeBizLogoAppBarPreview() {
    GaeBizLogoRightIconAppBar(
        logoDrawable = R.drawable.ggaebiz_kor,
        clickFirstRightIcon = {},
        clickSecondRightIcon = {}
    )
}
