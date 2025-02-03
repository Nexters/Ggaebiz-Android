package com.ggaebiz.ggaebiz.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme

@Composable
fun GaeBizTag(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle,
    radius: Int = 10,
    textColor: Color = GaeBizTheme.colors.gray900,
    backgroundColor: Color = GaeBizTheme.colors.gray75,
) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(radius.dp),
            )
            .padding(
                horizontal = 10.dp,
                vertical = 8.dp,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = textColor,
            style = textStyle,
        )
    }
}

@Preview("Tag")
@Composable
private fun GaeBizTagPreview() {
    GaeBizTag(
        text = stringResource(R.string.kiki_tag1_text),
        textStyle = GaeBizTheme.typography.bodySemiBold,
    )
}
