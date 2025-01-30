package com.ggaebiz.ggaebiz.presentation.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.White

@Composable
fun GgaebizLogoAppBar(
    modifier: Modifier = Modifier,
    logoDrawable: Int = R.drawable.ggaebiz_kor,
    color: Color = White,
) {
    Box(
        modifier = modifier
            .wrapContentSize(align = Alignment.Center)
            .background(color = color)
            .statusBarsPadding(),
    ) {
        Image(
            modifier = modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(vertical = 26.dp),
            painter = painterResource(id = logoDrawable),
            contentDescription = "Title Image",
        )
    }
}

@Preview("Image App Bar")
@Composable
private fun GgaebizLogoAppBarPreview() {
    GgaebizLogoAppBar(
        logoDrawable = R.drawable.ggaebiz_kor,
    )
}
