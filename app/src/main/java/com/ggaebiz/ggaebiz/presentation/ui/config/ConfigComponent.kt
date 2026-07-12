package com.ggaebiz.ggaebiz.presentation.ui.config

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Icon
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
import com.ggaebiz.ggaebiz.presentation.designsystem.component.icon.GaeBizIcon
import com.ggaebiz.ggaebiz.presentation.designsystem.component.slider.GaeBizSlider
import com.ggaebiz.ggaebiz.presentation.designsystem.component.switch.GaeBizSwitch
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.PretendardFont

@Composable
fun ConfigProfileCard(
    nickname: String,
    onClickEdit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClickEdit() }
            .background(GaeBizTheme.colors.white)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = stringResource(R.string.nickname_label),
                style = GaeBizTheme.typography.label3,
                color = GaeBizTheme.colors.gray200,
            )
            Text(
                text = nickname,
                style = GaeBizTheme.typography.titleBold,
                color = GaeBizTheme.colors.gray800,
            )
        }
        Spacer(Modifier.width(12.dp))
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = GaeBizIcon.icPencil,
            contentDescription = null,
            tint = GaeBizTheme.colors.gray200,
        )
    }
}

@Composable
fun ConfigAuthLinks(
    onClickLogout: () -> Unit,
    onClickWithdraw: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(GaeBizTheme.colors.gray75),
        )
        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.clickable { onClickLogout() },
                text = stringResource(R.string.config_logout),
                style = GaeBizTheme.typography.body2Medium,
                color = GaeBizTheme.colors.gray400,
            )
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = stringResource(R.string.config_link_divider),
                style = GaeBizTheme.typography.body2Medium,
                color = GaeBizTheme.colors.gray400,
            )
            Text(
                modifier = Modifier.clickable { onClickWithdraw() },
                text = stringResource(R.string.config_withdraw),
                style = GaeBizTheme.typography.body2Medium,
                color = GaeBizTheme.colors.gray400,
            )
        }
    }
}

@Composable
fun ConfigSliderSection(
    iconRes: Int,
    mainText: String,
    isSwitch: Boolean,
    switchValue: Boolean = true,
    onCheckedChange: () -> Unit = {},
    isSlider: Boolean = true,
    sliderValue: Int = 3,
    onSliderChange: (Int) -> Unit,
) {
    Row(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
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
            modifier = Modifier.weight(1f),
            text = mainText,
            style = GaeBizTheme.typography.bodySemiBold,
            color = GaeBizTheme.colors.gray800,
        )
        Spacer(Modifier.width(12.dp))
        if (isSwitch) {
            GaeBizSwitch(
                checked = switchValue,
                onCheckedChange = { onCheckedChange() },
            )
        }
    }
    if (isSlider) {
        Spacer(Modifier.height(8.dp))
        GaeBizSlider(
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(16.dp)
                )
                .background(GaeBizTheme.colors.gray50)
                .padding(horizontal = 16.dp, vertical = 4.dp),
            maxLevel = 10,
            minLevel = 1,
            initialLevel = sliderValue,
            onValueChange = { selectedLevel -> onSliderChange(selectedLevel) },
            thumbSize = 18.dp,
            thumbColor = GaeBizTheme.colors.gray900,
            trackHeight = 4.dp,
            activeTrackColor = GaeBizTheme.colors.gray900,
            inactiveTrackColor = GaeBizTheme.colors.gray75,
        )
    }
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
            modifier = Modifier
                .size(32.dp)
                .clip(
                    CircleShape
                )
                .background(GaeBizTheme.colors.gray50)
                .padding(8.dp),
            painter = painterResource(id = R.drawable.icon_battery),
            contentDescription = "Icon Image"
        )
        Spacer(Modifier.width(12.dp))
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.config_batter_title_text),
            style = GaeBizTheme.typography.bodySemiBold,
            color = GaeBizTheme.colors.gray800,
        )
        Spacer(Modifier.width(12.dp))
        Box(
            modifier = Modifier
                .height(32.dp)
                .clip(RoundedCornerShape(10.dp))
                .clickable { onClickCleatButton() }
                .background(GaeBizTheme.colors.gray800)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.config_batter_btn_text),
                style = GaeBizTheme.typography.label3,
                color = GaeBizTheme.colors.white
            )
        }
    }
    Spacer(Modifier.height(8.dp))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(16.dp)
            )
            .background(GaeBizTheme.colors.gray50)
            .padding(horizontal = 8.dp, vertical = 12.dp),
    ) {
        Text(
            text = stringResource(R.string.config_battery_sub_text1),
            style = GaeBizTheme.typography.label4,
            color = GaeBizTheme.colors.gray700,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = stringResource(R.string.config_battery_sub_text2),
            style = GaeBizTheme.typography.label4,
            color = GaeBizTheme.colors.gray700,
        )
    }
}
