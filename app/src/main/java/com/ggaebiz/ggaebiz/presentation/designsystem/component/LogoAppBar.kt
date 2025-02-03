package com.ggaebiz.ggaebiz.presentation.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.R

@Composable
fun GaeBizLogoAppBar(
    modifier: Modifier = Modifier,
    logoDrawable: Int = R.drawable.ggaebiz_kor,
) {
    Box(
        modifier = modifier
            .wrapContentSize(align = Alignment.Center)
            .statusBarsPadding(),
    ) {
        Image(
            modifier = modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(vertical = 26.dp),
            painter = painterResource(id = logoDrawable),
            contentDescription = stringResource(R.string.logo_img_description),
        )
    }
}

@Preview("Logo App Bar")
@Composable
private fun GaeBizLogoAppBarPreview() {
    GaeBizLogoAppBar(
        logoDrawable = R.drawable.ggaebiz_kor,
    )
}
