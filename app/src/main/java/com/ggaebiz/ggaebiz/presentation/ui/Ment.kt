package com.ggaebiz.ggaebiz.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import com.ggaebiz.ggaebiz.presentation.designsystem.icon.GaeBizIcon
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme

@Composable
fun GaeBizMent(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle,
    textColor: Color = GaeBizTheme.colors.gray800,
    radius: Int = 16,
    backgroundColor: Color = GaeBizTheme.colors.white,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                maxLines = 1,
                color = textColor,
                style = textStyle,
            )
        }
        Icon(
            modifier = Modifier.offset(y = (-9).dp),
            imageVector = GaeBizIcon.icPolygon,
            tint = backgroundColor,
            contentDescription = null,
        )
    }
}

@Preview("Ment")
@Composable
private fun GaeBizMentPreview() {
    GaeBizMent(
        text = stringResource(R.string.kiki_ment1_text),
        textStyle = GaeBizTheme.typography.titleSemiBold,
    )
}
