package com.ggaebiz.ggaebiz.presentation.ui.config

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.designsystem.component.slider.GaeBizSlider
import com.ggaebiz.ggaebiz.presentation.designsystem.component.switch.GaeBizSwitch
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.PretendardFont

@Composable
fun ConfigSliderSection(
    iconRes: Int,
    mainText: String,
    isSwitch: Boolean,
    switchValue: Boolean = true,
    onCheckedChange: () -> Unit,
    sliderValue: Int = 3,
    onSliderChange: () -> Unit,
) {
    Row(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Image(
            modifier = Modifier
                .size(32.dp)
                .clip(
                    CircleShape
                )
                .background(GaeBizTheme.colors.gray50)
                .padding(8.dp),
            painter = painterResource(id = iconRes),
            contentDescription = "Icon Image"
        )
        Spacer(Modifier.width(8.dp))
        Text(
            modifier = Modifier
                .padding(4.dp)
                .weight(1f),
            text = mainText,
            style = GaeBizTheme.typography.bodySemiBold,
            color = GaeBizTheme.colors.gray800,
        )
        Spacer(Modifier.width(12.dp))
        if (isSwitch) {
            GaeBizSwitch(
                checked = switchValue,
                enabled = true,
                onCheckedChange = { onCheckedChange() },
            )
        }
    }
    GaeBizSlider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(
                RoundedCornerShape(16.dp)
            )
            .background(GaeBizTheme.colors.gray50)
            .padding(horizontal = 16.dp, vertical = 4.dp),
        maxLevel = 10,
        initialLevel = sliderValue,
        onValueChange = { onSliderChange() },
        thumbSize = 18.dp,
        thumbColor = GaeBizTheme.colors.gray900,
        trackHeight = 4.dp,
        activeTrackColor = GaeBizTheme.colors.gray900,
        inactiveTrackColor = GaeBizTheme.colors.gray75,
    )
}

@Composable
fun ConfigBatterySection(
    onClickCleatButton: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            modifier = Modifier.size(32.dp),
            painter = painterResource(id = R.drawable.icon_battery),
            contentDescription = "Image"
        )
        Spacer(Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(R.string.config_batter_title_text),
                style = GaeBizTheme.typography.bodySemiBold,
                color = GaeBizTheme.colors.gray800,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.config_battery_sub_text),
                style = TextStyle(
                    fontFamily = PretendardFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 11.sp
                ),
                color = GaeBizTheme.colors.gray400,
            )
        }
        Text(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(10.dp)
                )
                .clickable { onClickCleatButton() }
                .background(GaeBizTheme.colors.gray50)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            text = stringResource(R.string.config_batter_btn_text),
            style = GaeBizTheme.typography.label3,
            color = GaeBizTheme.colors.black,
            textAlign = TextAlign.Center
        )
    }
}