package com.ggaebiz.ggaebiz.presentation.designsystem.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.designsystem.component.picker.GaeBizPicker
import com.ggaebiz.ggaebiz.presentation.designsystem.component.picker.PickerState
import com.ggaebiz.ggaebiz.presentation.designsystem.component.timer.TimerSmallColon
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme

@Composable
fun GaeBizTimePicker(
    modifier: Modifier = Modifier,
    hourPickerState: PickerState,
    minutePickerState: PickerState,
    visibleItemsCount: Int = 3,
    centerTextStyle: TextStyle = GaeBizTheme.typography.timer2,
    centerTextColor: Color = GaeBizTheme.colors.gray900,
    normalTextStyle: TextStyle = GaeBizTheme.typography.timer3,
    normalTextColor: Color = GaeBizTheme.colors.gray200,
    dividerHeight: Int = 2,
    normalDividerColor: Color = GaeBizTheme.colors.gray75,
    pressedDividerColor: Color = GaeBizTheme.colors.primaryOrange,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.wrapContentSize(),
    ) {
        val hours = (0..5).toList().map { it.toString().padStart(2, '0') }
        val minutes = (0..59).toList().map { it.toString().padStart(2, '0') }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(0.5f)) {
                GaeBizPicker(
                    pickerState = hourPickerState,
                    list = hours,
                    visibleItemsCount = visibleItemsCount,
                    centerTextStyle = centerTextStyle,
                    centerTextColor = centerTextColor,
                    normalTextStyle = normalTextStyle,
                    normalTextColor = normalTextColor,
                    dividerHeight = dividerHeight,
                    normalDividerColor = normalDividerColor,
                    pressedDividerColor = pressedDividerColor,
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.time_text),
                    modifier = Modifier.fillMaxWidth(),
                    color = GaeBizTheme.colors.gray400,
                    textAlign = TextAlign.Center,
                    style = GaeBizTheme.typography.body2SemiBold,
                )
            }

            TimerSmallColon(
                modifier = Modifier.padding(bottom = GaeBizTheme.typography.body2SemiBold.fontSize.value.dp + 12.dp)
            )

            Column(modifier = Modifier.weight(0.5f)) {
                GaeBizPicker(
                    pickerState = minutePickerState,
                    list = minutes,
                    visibleItemsCount = visibleItemsCount,
                    centerTextStyle = centerTextStyle,
                    centerTextColor = centerTextColor,
                    normalTextStyle = normalTextStyle,
                    normalTextColor = normalTextColor,
                    dividerHeight = dividerHeight,
                    normalDividerColor = normalDividerColor,
                    pressedDividerColor = pressedDividerColor,
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.minute_text),
                    modifier = Modifier.fillMaxWidth(),
                    color = GaeBizTheme.colors.gray400,
                    textAlign = TextAlign.Center,
                    style = GaeBizTheme.typography.body2SemiBold,
                )
            }
        }
    }
}

@Preview("Time Picker")
@Composable
private fun GaeBizTimePickerPreview() {
    GaeBizTimePicker(
        hourPickerState = PickerState(""),
        minutePickerState = PickerState(""),
    )
}
