package com.ggaebiz.ggaebiz.presentation.designsystem.component.picker

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Composable
fun GaeBizPicker(
    modifier: Modifier = Modifier,
    pickerState: PickerState,
    list: List<String>,
    visibleItemsCount: Int,
    centerTextStyle: TextStyle,
    centerTextColor: Color,
    normalTextStyle: TextStyle,
    normalTextColor: Color,
    dividerHeight: Int,
    normalDividerColor: Color,
    pressedDividerColor: Color,
    onScrollFinished: () -> Unit,
) {
    val listScrollCount = Integer.MAX_VALUE
    val listScrollMiddle = listScrollCount / 2

    val visibleItemsMiddle = visibleItemsCount / 2
    val listStartIndex = listScrollMiddle - listScrollMiddle % list.size - visibleItemsMiddle + list.indexOf(pickerState.selectedItem)

    val coroutineScope = rememberCoroutineScope()
    var dividerColor by remember { mutableStateOf(normalDividerColor) }
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = listStartIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    val normalItemHeightPixels = remember { mutableStateOf(0) }
    val normalItemHeightDp = pixelsToDp(normalItemHeightPixels.value)
    val centerItemHeightPixels = remember { mutableStateOf(0) }
    val centerIemHeightDp = pixelsToDp(centerItemHeightPixels.value)

    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .distinctUntilChanged()
            .collect {
                dividerColor = if (listState.isScrollInProgress) {
                    pressedDividerColor
                } else {
                    normalDividerColor
                }
            }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset }
            .map { (index, offset) -> index + visibleItemsMiddle to offset }
            .distinctUntilChanged()
            .collect { (index, offset) ->
                pickerState.selectedItem = list[index % list.size]
                
                if (offset == 0) onScrollFinished()

                if (offset > 0) {
                    coroutineScope.launch {
                        val scrollAmount = -offset.toFloat()
                        listState.animateScrollBy(
                            value = scrollAmount,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioNoBouncy,
                                stiffness = Spring.StiffnessMedium,
                            ),
                        )
                    }
                }
            }
    }

    Box(modifier = modifier) {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .height(normalItemHeightDp * (visibleItemsCount - 1) + centerIemHeightDp + dividerHeight.dp * 2),
        ) {
            items(listScrollCount) { index ->
                val item = list[index % list.size]
                val isSelected = item == pickerState.selectedItem
                val color = if (isSelected) centerTextColor else normalTextColor
                val textStyle = if (isSelected) centerTextStyle else normalTextStyle

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .wrapContentHeight(Alignment.CenterVertically),
                ) {
                    if (isSelected) Spacer(modifier = Modifier.height(dividerHeight.dp))
                    Text(
                        text = item,
                        style = textStyle.copy(color = color),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .onSizeChanged { size ->
                                if (isSelected) {
                                    centerItemHeightPixels.value = size.height
                                } else {
                                    normalItemHeightPixels.value = size.height
                                }
                            }
                            .height(textStyle.fontSize.value.dp),
                    )
                    if (isSelected) Spacer(modifier = Modifier.height(dividerHeight.dp))
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .offset(y = normalItemHeightDp * visibleItemsMiddle)
                .height(dividerHeight.dp),
            color = dividerColor,
        )

        HorizontalDivider(
            modifier = Modifier
                .offset(y = normalItemHeightDp * visibleItemsMiddle + centerIemHeightDp + dividerHeight.dp * 2)
                .height(dividerHeight.dp),
            color = dividerColor,
        )
    }
}

@Composable
private fun pixelsToDp(pixels: Int) = with(LocalDensity.current) { pixels.toDp() }

@Composable
fun rememberPickerState(defaultValue: String = "") = remember { PickerState(defaultValue) }

class PickerState(defaultValue: String) {
    var selectedItem by mutableStateOf(defaultValue)
}

@Preview("Picker")
@Composable
private fun GaeBizPickerPreview() {
    val hours = (0..6).toList().map { it.toString().padStart(2, '0') }
    val hourPickerState = rememberPickerState()

    GaeBizPicker(
        pickerState = hourPickerState,
        list = hours,
        visibleItemsCount = 3,
        centerTextStyle = GaeBizTheme.typography.timer2,
        centerTextColor = GaeBizTheme.colors.gray900,
        normalTextStyle = GaeBizTheme.typography.timer3,
        normalTextColor = GaeBizTheme.colors.gray200,
        dividerHeight = 2,
        normalDividerColor = GaeBizTheme.colors.gray75,
        pressedDividerColor = GaeBizTheme.colors.primaryOrange,
        onScrollFinished = { },
    )
}
