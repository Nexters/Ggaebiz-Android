package com.ggaebiz.ggaebiz.presentation.designsystem.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme

@Composable
fun GaeBizButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    radius: Int = 15,
    contentColor: Color,
    containerColor: Color,
    text: String,
    style: TextStyle,
    textColor: Color,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(radius.dp),
        colors = ButtonColors(
            contentColor = contentColor,
            containerColor = containerColor,
            disabledContentColor = containerColor,
            disabledContainerColor = contentColor,
        ),
        content = {
            Text(
                modifier = Modifier.padding(
                    horizontal = 20.dp,
                    vertical = 12.dp,
                ),
                text = text,
                style = style,
                color = textColor,
            )
        }
    )
}

@Preview("Button")
@Composable
private fun GaeBizButtonPreview() {
    GaeBizButton(
        onClick = {},
        contentColor = GaeBizTheme.colors.white,
        containerColor = GaeBizTheme.colors.gray800,
        text = stringResource(R.string.setting_button_text),
        style = GaeBizTheme.typography.bodySemiBold,
        textColor = GaeBizTheme.colors.white,
    )
}
