package com.ggaebiz.ggaebiz.presentation.designsystem.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.Gray800
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.White

@Composable
fun GgaebizButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    radius: Int = 15,
    contentColor: Color,
    containerColor: Color,
    text: @Composable () -> Unit,
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
            text()
        }
    )
}


@Preview("Button")
@Composable
private fun GgaebizButtonPreview() {
    GgaebizButton(
        onClick = {},
        contentColor = White,
        containerColor = Gray800,
    ) {
        Text(text = "키키랑 타이머 설정하기")
    }
}
