package com.ggaebiz.ggaebiz.presentation.ui

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.designsystem.component.GaeBizPicker
import com.ggaebiz.ggaebiz.presentation.designsystem.component.PickerState
import com.ggaebiz.ggaebiz.presentation.designsystem.component.rememberPickerState
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme

@Composable
fun GaeBizTimePicker(
    modifier: Modifier = Modifier,
    hourPickerState: PickerState = rememberPickerState(),
    minutePickerState: PickerState = rememberPickerState(),
    startIndex: Int = 0,
    visibleItemsCount: Int = 3,
    centerTextStyle: TextStyle = GaeBizTheme.typography.timer2,
    centerTextColor: Color = GaeBizTheme.colors.gray900,
    normalTextStyle: TextStyle = GaeBizTheme.typography.timer3,
    normalTextColor: Color = GaeBizTheme.colors.gray200,
    dividerHeight: Int = 2,
    normalDividerColor: Color = GaeBizTheme.colors.gray75,
    pressedDividerColor: Color = GaeBizTheme.colors.primaryOrange,
    onScrollFinished: (String) -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.wrapContentSize(),
    ) {
        val hours = (0..6).toList().map { it.toString().padStart(2, '0') }
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
                    startIndex = startIndex,
                    visibleItemsCount = visibleItemsCount,
                    centerTextStyle = centerTextStyle,
                    centerTextColor = centerTextColor,
                    normalTextStyle = normalTextStyle,
                    normalTextColor = normalTextColor,
                    dividerHeight = dividerHeight,
                    normalDividerColor = normalDividerColor,
                    pressedDividerColor = pressedDividerColor,
                    onScrollFinished = onScrollFinished,
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

            Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                Text(
                    text = ":",
                    style = GaeBizTheme.typography.timer2,
                )
                Spacer(
                    Modifier.height(GaeBizTheme.typography.body2SemiBold.fontSize.value.dp + 12.dp),
                )
            }

            Column(modifier = Modifier.weight(0.5f)) {
                GaeBizPicker(
                    pickerState = minutePickerState,
                    list = minutes,
                    startIndex = startIndex,
                    visibleItemsCount = visibleItemsCount,
                    centerTextStyle = centerTextStyle,
                    centerTextColor = centerTextColor,
                    normalTextStyle = normalTextStyle,
                    normalTextColor = normalTextColor,
                    dividerHeight = dividerHeight,
                    normalDividerColor = normalDividerColor,
                    pressedDividerColor = pressedDividerColor,
                    onScrollFinished = onScrollFinished,
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
    GaeBizTimePicker()
}
