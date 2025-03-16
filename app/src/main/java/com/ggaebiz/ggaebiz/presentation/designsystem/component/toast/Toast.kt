package com.ggaebiz.ggaebiz.presentation.designsystem.component.toast

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.presentation.designsystem.component.icon.GaeBizIcon
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme

@Composable
fun GaebizToast(
    modifier:Modifier = Modifier,
    ment: String,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 23.5.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(GaeBizTheme.colors.black44, shape = RoundedCornerShape(15.dp))
                .padding(horizontal = 14.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.wrapContentSize(),
                imageVector = GaeBizIcon.icOrangeSpeaker,
                tint = Color.Unspecified,
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(10.dp))
            
            Text(
                text = ment,
                color = Color.White,
                style = GaeBizTheme.typography.body2Medium
            )
        }
    }
}

@Preview("Toast")
@Composable
private fun GaeBizToastPreview() {
    GaebizToast(ment = "{키키}가 말 하고 있어요. 볼륨을 켜주세요.")
}
