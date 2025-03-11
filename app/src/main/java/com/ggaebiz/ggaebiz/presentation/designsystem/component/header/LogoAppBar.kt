package com.ggaebiz.ggaebiz.presentation.designsystem.component.header

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.designsystem.component.button.GaeBizIconButton
import com.ggaebiz.ggaebiz.presentation.designsystem.component.icon.GaeBizIcon

@Composable
fun GaeBizLogoAppBar(
    modifier: Modifier = Modifier,
    logoDrawable: Int = R.drawable.ggaebiz_kor,
    clickRightIcon : () -> Unit
) {
    Box(
        modifier = modifier.fillMaxWidth().padding(horizontal = 20.dp)
    ) {
        Image(
            modifier = modifier
                .height(64.dp)
                .padding(vertical = 22.dp),
            painter = painterResource(id = logoDrawable),
            contentDescription = stringResource(R.string.logo_img_description),
        )

        Image(
            painter = painterResource(id = R.drawable.ic_setting),
            contentDescription = "Clickable Image",
            modifier = Modifier.size(48.dp).clickable {
                clickRightIcon()
            }.align(Alignment.CenterEnd)
        )

    }
}

@Preview("Logo App Bar")
@Composable
private fun GaeBizLogoAppBarPreview() {
    GaeBizLogoAppBar(
        logoDrawable = R.drawable.ggaebiz_kor,
        clickRightIcon = {}
    )
}
