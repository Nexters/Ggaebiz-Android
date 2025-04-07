package com.ggaebiz.ggaebiz.presentation.ui.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.designsystem.component.icon.GaeBizIcon
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme

@Composable
fun SettingNudgePopup(
    density : Density,
    spacerHeightPx : Float,
    closeClick : () -> Unit
){
    val yOffset = with(density) { (spacerHeightPx).toDp() }
    Column(
        modifier = Modifier
            .offset(y = -yOffset)
            .clickable(enabled = false) {}
    ){
        Icon(
            modifier = Modifier
                .offset(y = (9).dp)
                .padding(start = 2.dp),
            imageVector = GaeBizIcon.icAbovePolygon,
            tint = GaeBizTheme.colors.white,
            contentDescription = null,
        )
        Column(
            modifier = Modifier
                .width(200.dp)
                .background(
                    color = GaeBizTheme.colors.white,
                    shape = RoundedCornerShape(16.dp),
                )
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp,
                )
        ) {
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = stringResource(R.string.timer_setting_nudge_guide),
                color = GaeBizTheme.colors.gray800,
                style = GaeBizTheme.typography.body2Medium,
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 8.dp)
                    .clickable { closeClick() },
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = stringResource(R.string.timer_setting_nudge_guide_close),
                    color = GaeBizTheme.colors.primaryOrange,
                    style = GaeBizTheme.typography.body2Medium
                )
            }
        }
    }
}