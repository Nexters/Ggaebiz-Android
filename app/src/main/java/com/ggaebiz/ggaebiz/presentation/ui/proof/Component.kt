package com.ggaebiz.ggaebiz.presentation.ui.proof

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import com.ggaebiz.ggaebiz.presentation.model.TimeStampStyleType
import com.ggaebiz.ggaebiz.presentation.ui.proof.editor.generateTimeStampSources
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.DigitalNumbersFont
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.PoppinsFont
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.AgbalumoFont
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.TimeLineFont
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.ZuumeFont
import com.ggaebiz.ggaebiz.presentation.model.Sticker
import com.ggaebiz.ggaebiz.presentation.model.StickerSource
import com.ggaebiz.ggaebiz.presentation.ui.proof.editor.TimeStampColor
import com.ggaebiz.ggaebiz.presentation.ui.proof.editor.TimeStampStyle
import com.ggaebiz.ggaebiz.presentation.ui.proof.editor.toStyle
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Segmented2Tabs(
    left: String,
    right: String,
    selectedRight: Boolean,
    onSelectLeft: () -> Unit,
    onSelectRight: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, bottom = 16.dp),
        shape = RoundedCornerShape(14.dp),
        color = GaeBizTheme.colors.gray500
    ) {
        Row(
            Modifier.padding(6.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        )
        {
            Pill(
                text = left,
                selected = !selectedRight,
                onClick = onSelectLeft,
                modifier = Modifier.weight(1f)
            )
            Pill(
                text = right,
                selected = selectedRight,
                onClick = onSelectRight,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun Pill(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bg = if (selected) GaeBizTheme.colors.white else Color.Transparent
    val textC = if (selected) GaeBizTheme.colors.primaryOrange else GaeBizTheme.colors.gray50
    val textS =
        (if (selected) GaeBizTheme.typography.body2SemiBold else GaeBizTheme.typography.body2SemiBold)
    val interaction = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bg)
            .clickable(
                interactionSource = interaction,
                indication = null, onClick = onClick
            )
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, style = textS, color = textC, maxLines = 1)
    }
}


@Composable
fun StickersCanvas(
    canvasSize: IntSize,
    stickers: List<Sticker>,
    selectedId: String?,
    onSelect: (String) -> Unit,
    onMove: (id: String, x: Float, y: Float) -> Unit,
    onScale: (id: String, scale: Float) -> Unit,
    onRotate: (id: String, angleDelta: Float) -> Unit,
    onRemove: (id: String) -> Unit,
) {
    val density = LocalDensity.current
    val baseSizePx = with(density) { 120.dp.toPx() }

    stickers.forEach { s ->
        key(s.id) {
            val isSelected = s.id == selectedId
            val sizePx = baseSizePx * s.scale
            val sizeDp = with(density) { sizePx.toDp() }

            val currentOffset by rememberUpdatedState(s.offset)
            val currentScale by rememberUpdatedState(s.scale)
            val currentRotation by rememberUpdatedState(s.rotation)
            val isTimeStamp = s.source is StickerSource.TimeStamp
            var measuredSize by remember(s.id) { mutableStateOf(IntSize.Zero) }
            Box(
                modifier = Modifier
                    .offset {
                        if (isTimeStamp) {
                            IntOffset(
                                (s.offset.x - measuredSize.width * s.scale / 2f).toInt(),
                                (s.offset.y - measuredSize.height * s.scale / 2f).toInt()
                            )
                        } else {
                            IntOffset(
                                (s.offset.x - sizePx / 2f).toInt(),
                                (s.offset.y - sizePx / 2f).toInt()
                            )
                        }
                    }
                    .then(if (isTimeStamp) Modifier.wrapContentSize() else Modifier.size(sizeDp))
                    .onSizeChanged { if (isTimeStamp) measuredSize = it }
                    .graphicsLayer {
                        if (isTimeStamp) {
                            scaleX = s.scale; scaleY = s.scale
                        }
                        rotationZ = s.rotation
                    }
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onSelect(s.id)
                    }
                    .pointerInput(s.id) {
                        var startX = 0f
                        var startY = 0f
                        var accX = 0f
                        var accY = 0f
                        detectDragGestures(
                            onDragStart = {
                                onSelect(s.id)
                                startX = currentOffset.x
                                startY = currentOffset.y
                                accX = 0f
                                accY = 0f
                            },
                            onDrag = { change, delta ->
                                change.consume()
                                accX += delta.x
                                accY += delta.y
                                val nx = (startX + accX)
                                    .coerceIn(0f, canvasSize.width.toFloat())
                                val ny = (startY + accY)
                                    .coerceIn(0f, canvasSize.height.toFloat())
                                onMove(s.id, nx, ny)
                            }
                        )
                    }
            ) {
                when (val src = s.source) {
                    is StickerSource.Png -> Image(
                        painter = painterResource(id = src.resId),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )

                    is StickerSource.TimeStamp -> TimeStampSticker(source = src)
                }

                if (isSelected) {
                    val handleSize = 24.dp
                    val handleOffset = 8.dp

                    // 선택 테두리
                    Box(
                        Modifier
                            .matchParentSize()
                            .border(1.dp, GaeBizTheme.colors.white)
                    )

                    // 좌상단: 삭제
                    Box(
                        Modifier
                            .size(handleSize)
                            .align(Alignment.TopStart)
                            .offset(x = -handleOffset, y = -handleOffset)
                            .background(GaeBizTheme.colors.white, CircleShape)
                            .clickable { onRemove(s.id) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.icon_image_delete),
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(12.dp)
                        )
                    }

                    // 우하단: 리사이즈 + 회전
                    Box(
                        Modifier
                            .size(handleSize)
                            .align(Alignment.BottomEnd)
                            .offset(x = handleOffset, y = handleOffset)
                            .background(GaeBizTheme.colors.white, CircleShape)
                            .pointerInput(s.id) {
                                var startScale = 1f
                                var accRadial = 0f
                                detectDragGestures(
                                    onDragStart = {
                                        startScale = currentScale
                                        accRadial = 0f
                                    },
                                    onDrag = { change, delta ->
                                        change.consume()
                                        // 화면 좌표 → 스티커 로컬 좌표 변환 (회전 보정)
                                        val rot = currentRotation * (PI.toFloat() / 180f)
                                        val cosR = cos(rot)
                                        val sinR = sin(rot)
                                        val localDx = delta.x * cosR + delta.y * sinR
                                        val localDy = -delta.x * sinR + delta.y * cosR

                                        // 방사형 (NW↔SE): 크기 조절
                                        val radial = (localDx + localDy) / 2f
                                        accRadial += radial
                                        val newScale = (startScale + accRadial / baseSizePx)
                                            .coerceIn(0.3f, 5f)
                                        onScale(s.id, newScale)

                                        // 접선형 (수직 방향): 회전
                                        val tangential = (-localDx + localDy) / 2f
                                        val halfDiag = baseSizePx * currentScale * 0.7f
                                        val angleDelta =
                                            tangential / halfDiag * (180f / PI.toFloat())
                                        onRotate(s.id, angleDelta)
                                    }
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.icon_image_resize),
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StickerPickerGrid(
    sources: List<StickerSource>,
    onPick: (StickerSource) -> Unit,
    columns: Int = 3,
    horizontalPadding: Dp = 16.dp,
    horizontalSpacing: Dp = 8.dp,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp, max = 300.dp)
            .nestedScroll(rememberNestedScrollInteropConnection())
            .padding(horizontal = horizontalPadding, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(horizontalSpacing)
    ) {
        items(sources.size) { idx ->
            val src = sources[idx]
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(if (src is StickerSource.Png) Modifier.aspectRatio(1f) else Modifier)
                    .clickable { onPick(src) },
                contentAlignment = Alignment.Center
            ) {
                when (src) {
                    is StickerSource.Png -> Image(
                        painter = painterResource(id = src.resId),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )

                    is StickerSource.TimeStamp -> TimeStampStickerPreview(source = src)
                }
            }
        }
    }
}

@Composable
fun TimeStampSticker(source: StickerSource.TimeStamp) {
    when (val style = source.styleType.toStyle()) {
        is TimeStampStyle.Zuume -> TimestampZuume(source.text, style.color)
        is TimeStampStyle.Poppins -> TimestampPoppins(source.text, style.color)
        is TimeStampStyle.Digital -> TimestampDigital(source.text, style.color)
        is TimeStampStyle.Timeline -> TimestampTimeline(source.text, style.color)
        is TimeStampStyle.Agbalumo -> TimestampAgbalumo(source.text, style.color)
        is TimeStampStyle.PoppinsTimeFormat -> TimestampPoppinsTimeFormat(source.text, style.color)
        is TimeStampStyle.PoppinsClockFormat -> TimestampPoppinsClockFormat(
            source.text,
            style.color
        )
    }
}

@Composable
private fun TimeStampStickerPreview(source: StickerSource.TimeStamp) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        contentAlignment = Alignment.Center,
    ) {
        val stickerSize = source.styleType.previewSize
        val scale = minOf(1f, maxWidth / stickerSize.width)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(stickerSize.height * scale),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .requiredSize(stickerSize.width, stickerSize.height)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    },
                contentAlignment = Alignment.Center,
            ) {
                TimeStampSticker(source = source)
            }
        }
    }
}

private val TimeStampStyleType.previewSize
    get() = when (this) {
        TimeStampStyleType.ORANGE_ZUUME,
        TimeStampStyleType.GRAY_ZUUME,
        TimeStampStyleType.DARK_ZUUME,
            -> DpSize(width = 140.dp, height = 80.dp)

        TimeStampStyleType.DARK_DIGITAL -> DpSize(width = 140.dp, height = 64.dp)

        TimeStampStyleType.ORANGE_POPPINS,
        TimeStampStyleType.GRAY_POPPINS,
            -> DpSize(width = 128.dp, height = 64.dp)

        TimeStampStyleType.WIHTE_TIMELINE,
        TimeStampStyleType.GRAY_TIMELINE,
            -> DpSize(width = 137.dp, height = 80.dp)

        TimeStampStyleType.WHITE_AGBALUMO,
        TimeStampStyleType.GRAY_AGBALUMO,
            -> DpSize(width = 124.dp, height = 80.dp)

        TimeStampStyleType.ORANGE_POPPINS_TIME_FORMAT,
        TimeStampStyleType.GRAY_POPPINS_TIME_FORMAT,
            -> DpSize(width = 150.dp, height = 56.dp)

        TimeStampStyleType.WHITE_POPPINS_TIME_CLOCK_FORMAT,
        TimeStampStyleType.GRAY_POPPINS_TIME_CLOCK_FORMAT,
            -> DpSize(width = 80.dp, height = 140.dp)
    }

@Composable
private fun TimestampZuume(text: String, color: TimeStampColor) {
    val hasBackground = color.backgroundColor != null
    Box(
        modifier = Modifier
            .background(color.backgroundColor ?: Color.Transparent, RoundedCornerShape(40))
            .size(width = 140.dp, height = 80.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            fontFamily = ZuumeFont,
            text = text,
            color = color.textColor,
            fontWeight = FontWeight.SemiBold,
            fontSize = 40.sp,
            lineHeight = 80.sp,
            letterSpacing = 1.sp,
            maxLines = 1,
        )
    }
}

@Composable
private fun TimestampPoppins(text: String, color: TimeStampColor) {
    Box(
        modifier = Modifier
            .background(color.backgroundColor ?: Color.Transparent, RoundedCornerShape(40))
            .size(width = 128.dp, height = 64.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            fontFamily = PoppinsFont,
            text = text,
            color = color.textColor,
            fontWeight = FontWeight.SemiBold,
            fontSize = 32.sp,
            lineHeight = 64.sp,
            maxLines = 1,
        )
    }
}


@Composable
private fun TimestampDigital(text: String, color: TimeStampColor) {
    val parts = text.split(":", limit = 2)
    val hour = parts.getOrElse(0) { text }.trim()
    val minute = parts.getOrElse(1) { "" }.trim()

    Box(
        modifier = Modifier
            .background(color.backgroundColor ?: Color.Transparent, RoundedCornerShape(12))
            .size(width = 140.dp, height = 64.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                fontFamily = DigitalNumbersFont,
                text = hour,
                color = color.textColor,
                fontWeight = FontWeight.Normal,
                fontSize = 32.sp,
                lineHeight = 64.sp,
                maxLines = 1,
            )
            Text(
                text = ":",
                color = color.textColor,
                fontWeight = FontWeight.Normal,
                fontFamily = DigitalNumbersFont,
                fontSize = 32.sp,
                lineHeight = 64.sp,
                maxLines = 1,
                modifier = Modifier.width(10.dp),
                textAlign = TextAlign.Center,
            )
            Text(
                fontFamily = DigitalNumbersFont,
                text = minute,
                color = color.textColor,
                fontWeight = FontWeight.Normal,
                fontSize = 32.sp,
                lineHeight = 64.sp,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun TimestampTimeline(text: String, color: TimeStampColor) {
    Box(
        modifier = Modifier.size(width = 137.dp, height = 80.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            fontFamily = TimeLineFont,
            text = text,
            color = color.textColor,
            fontWeight = FontWeight.Normal,
            fontSize = 40.sp,
            lineHeight = 80.sp,
            maxLines = 1,
            letterSpacing = 1.sp
        )
    }
}

@Composable
private fun TimestampAgbalumo(text: String, color: TimeStampColor) {
    Box(
        modifier = Modifier
            .size(width = 124.dp, height = 80.dp),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Text(
            fontFamily = AgbalumoFont,
            text = text,
            color = color.textColor,
            fontWeight = FontWeight.Normal,
            fontSize = 40.sp,
            lineHeight = 80.sp,
            maxLines = 1,
            letterSpacing = 1.sp
        )
    }
}

@Composable
private fun TimestampPoppinsTimeFormat(text: String, color: TimeStampColor) {
    Box(
        modifier = Modifier
            .background(color.backgroundColor ?: Color.Transparent, RoundedCornerShape(50))
            .size(width = 150.dp, height = 56.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            fontFamily = PoppinsFont,
            text = text,
            color = color.textColor,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            lineHeight = 48.sp,
            maxLines = 1,
            letterSpacing = 1.sp
        )
    }
}

@Composable
private fun TimestampPoppinsClockFormat(text: String, color: TimeStampColor) {
    val parts = text.split(":")
    val top = parts.getOrElse(0) { text }.trim()
    val bottom = parts.getOrElse(1) { "" }.trim()
    Box(
        modifier = Modifier.size(width = 80.dp, height = 140.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            fontFamily = PoppinsFont,
            text = "$top\n$bottom",
            color = color.textColor,
            fontWeight = FontWeight.Medium,
            fontSize = 40.sp,
            lineHeight = 40.sp,
            maxLines = 2,
            letterSpacing = 1.sp
        )
    }
}


// ─── Previews ────────────────────────────────────────────────────────────────

@Preview(name = "Tabs - 타임스탬프 선택", showBackground = true, apiLevel = 34)
@Composable
private fun Segmented2TabsLeftPreview() {
    GaeBizTheme {
        Segmented2Tabs(
            left = "타임 스탬프",
            right = "스티커",
            selectedRight = false,
            onSelectLeft = {},
            onSelectRight = {},
        )
    }
}

@Preview(name = "Tabs - 스티커 선택", showBackground = true, apiLevel = 34)
@Composable
private fun Segmented2TabsRightPreview() {
    GaeBizTheme {
        Segmented2Tabs(
            left = "타임 스탬프",
            right = "스티커",
            selectedRight = true,
            onSelectLeft = {},
            onSelectRight = {},
        )
    }
}

@Preview(
    name = "StickerPickerGrid - 타임스탬프 (2열, 전체)",
    showBackground = true,
    backgroundColor = 0xFF1C1C1C,
    apiLevel = 34
)
@Composable
private fun StickerPickerGridTimestampPreview() {
    GaeBizTheme {
        val sources = generateTimeStampSources(hour = 0, minute = 30)
        StickerPickerGrid(
            sources = sources,
            columns = 2,
            onPick = {},
        )
    }
}

@Preview(
    name = "StickerPickerGrid - 스티커 (3열)",
    showBackground = true,
    backgroundColor = 0xFF1C1C1C,
    apiLevel = 34
)
@Composable
private fun StickerPickerGridStickerPreview() {
    GaeBizTheme {
        StickerPickerGrid(
            sources = listOf(
                StickerSource.Png(R.drawable.sticker_1),
                StickerSource.Png(R.drawable.sticker_2),
                StickerSource.Png(R.drawable.sticker_3),
                StickerSource.Png(R.drawable.sticker_4),
                StickerSource.Png(R.drawable.sticker_5),
                StickerSource.Png(R.drawable.sticker_6),
            ),
            columns = 3,
            onPick = {},
        )
    }
}

@Preview(
    name = "TimeStampSticker - 전체 스타일",
    showBackground = true,
    backgroundColor = 0xFF333333,
    apiLevel = 34
)
@Composable
private fun TimeStampStickerAllStylesPreview() {
    GaeBizTheme {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TimeStampStyleType.entries.forEach { styleType ->
                val text = if (styleType == TimeStampStyleType.ORANGE_ZUUME) "0h 30min" else "00:30"
                TimeStampSticker(
                    source = StickerSource.TimeStamp(
                        text = text,
                        styleType = styleType
                    )
                )
            }
        }
    }
}
