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
fun GaeBizSpeechText(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle,
    textColor: Color = GaeBizTheme.colors.gray800,
    radius: Int = 16,
    backgroundColor: Color = GaeBizTheme.colors.white,
) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(radius.dp),
            )
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp,
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

@Preview("Speech Text")
@Composable
private fun GaeBizSpeechTextPreview() {
    GaeBizSpeechText(
        text = stringResource(R.string.kiki_speech1_text),
        textStyle = GaeBizTheme.typography.titleSemiBold,
    )
}
