package com.ggaebiz.ggaebiz.presentation.ui.proof

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import com.ggaebiz.ggaebiz.presentation.model.BitmapSticker
import com.ggaebiz.ggaebiz.presentation.model.Sticker
import com.ggaebiz.ggaebiz.presentation.model.StickerSource
import kotlin.math.PI
import kotlin.math.atan2

@Composable
fun Segmented2Tabs(
    left: String,
    right: String,
    selectedRight: Boolean,
    onSelectLeft: () -> Unit,
    onSelectRight: () -> Unit,
    modifier: Modifier = Modifier
) {
    val stroke = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp , bottom = 16.dp),
        shape = RoundedCornerShape(14.dp),
        color = GaeBizTheme.colors.gray500
    ) {
        Row(Modifier.padding(6.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp))
            {
            Pill(text = left,  selected = !selectedRight,  onClick = onSelectLeft,  modifier = Modifier.weight(1f))
            Pill(text = right, selected =  selectedRight, onClick = onSelectRight, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun Pill(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bg  =if (selected) GaeBizTheme.colors.white else Color.Transparent
    val textC=if (selected) GaeBizTheme.colors.primaryOrange else GaeBizTheme.colors.gray50
    val textS = (if ( selected) GaeBizTheme.typography.body2SemiBold else GaeBizTheme.typography.body2SemiBold )
    val interaction = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bg)
            .clickable(
                interactionSource = interaction,
                indication = null
                , onClick = onClick)
            .padding(horizontal = 10.dp , vertical = 6.dp)
            ,
        contentAlignment = Alignment.Center
    ) {
        Text(text, style = textS ,color = textC, maxLines = 1)
    }
}

@Composable
fun StickersCanvas(
    canvasSize: IntSize,
    stickers: List<Sticker>,
    selectedId: String?,
    onSelect: (String) -> Unit,
    onBringToFront: (String) -> Unit,
    onMove: (id: String, x: Float, y: Float) -> Unit,
    onResize: (id: String, newScale: Float) -> Unit,
    onRotate: (id: String, delta: Float) -> Unit,
    onRemove: (id: String) -> Unit,
    edgeOverdrag: Dp = 0.dp,
    onDragActiveChange: (Boolean) -> Unit = {},
) {
    val density = LocalDensity.current
    val baseSizeDp = 120.dp
    val baseSizePx = with(density) { baseSizeDp.toPx() }
    val overPx = with(density) { edgeOverdrag.toPx() }

    stickers.forEach { s ->
        val isSel = s.id == selectedId
        val currW = baseSizePx * s.scale
        val currH = baseSizePx * s.scale
        val halfW = currW / 2f
        val halfH = currH / 2f

        val minX = halfW - overPx
        val maxX = canvasSize.width - halfW + overPx
        val minY = halfH - overPx
        val maxY = canvasSize.height - halfH + overPx

        val minXState by rememberUpdatedState(minX)
        val maxXState by rememberUpdatedState(maxX)
        val minYState by rememberUpdatedState(minY)
        val maxYState by rememberUpdatedState(maxY)

        Box(
            modifier = Modifier
                .graphicsLayer {
                    translationX = s.x - halfW
                    translationY = s.y - halfH
                    scaleX = s.scale
                    scaleY = s.scale
                    rotationZ = s.rotation
                }
                .size(baseSizeDp * s.scale)
                .pointerInput(s.id) {
                    var startX = 0f
                    var startY = 0f
                    var accX = 0f
                    var accY = 0f
                    detectDragGestures(
                        onDragStart = {
                            onSelect(s.id)
                            onBringToFront(s.id)
                            startX = s.x
                            startY = s.y
                            accX = 0f
                            accY = 0f
                            onDragActiveChange(true)
                        },
                        onDragEnd = { onDragActiveChange(false) },
                        onDragCancel = { onDragActiveChange(false) },
                        onDrag = { change, drag ->
                            change.consume()
                            accX += drag.x
                            accY += drag.y
                            val nx = (startX + accX).coerceIn(minXState, maxXState)
                            val ny = (startY + accY).coerceIn(minYState, maxYState)
                            onMove(s.id, nx, ny)
                        }
                    )
                }
        ) {
            when (s) {
                is BitmapSticker -> Image(
                    bitmap = s.source.image,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }

            if (isSel) {
                val borderW = 1.dp
                val delHandleSize = 20.dp
                val resizeHandleSize = 24.dp
                val innerBiasDp = 4.dp

                val halfBorderPx = with(density) { (borderW / 2).toPx() }
                val halfDelPx = with(density) { (delHandleSize / 2).toPx() }
                val halfResizePx = with(density) { (resizeHandleSize / 2).toPx() }
                val innerBiasPx = with(density) { innerBiasDp.toPx() }

                Box(
                    Modifier
                        .matchParentSize()
                        .border(borderW, GaeBizTheme.colors.white)
                )

                var lastAngle by remember { mutableStateOf(0f) }
                Box(
                    Modifier
                        .size(resizeHandleSize)
                        .align(Alignment.BottomEnd)
                        .offset {
                            IntOffset(
                                (halfResizePx + halfBorderPx - innerBiasPx).toInt(),
                                (halfResizePx + halfBorderPx - innerBiasPx).toInt()
                            )
                        }
                        .background(GaeBizTheme.colors.white, CircleShape)
                        .pointerInput(s.id) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    lastAngle = atan2(offset.y - s.y, offset.x - s.x)
                                },
                                onDrag = { change, drag ->
                                    change.consume()
                                    // 스케일
                                    val deltaScale = (drag.x + drag.y) / 200f
                                    onResize(s.id, (s.scale + deltaScale).coerceAtLeast(0.1f))
                                    // 회전
                                    val currentAngle =
                                        atan2(change.position.y - s.y, change.position.x - s.x)
                                    val delta = -(currentAngle - lastAngle) * 180f / PI.toFloat()
                                    lastAngle = currentAngle
                                    onRotate(s.id, delta)
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

                // 좌상단: 삭제
                Box(
                    Modifier
                        .size(delHandleSize)
                        .align(Alignment.TopStart)
                        .offset {
                            IntOffset(
                                -(halfDelPx + halfBorderPx).toInt(),
                                -(halfDelPx + halfBorderPx).toInt()
                            )
                        }
                        .background(GaeBizTheme.colors.white, CircleShape)
                        .clickable { onRemove(s.id) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.icon_image_delete),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(10.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun StickerPickerGrid(
    sources: List<StickerSource>,
    onPick: (StickerSource) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp, max = 300.dp)
            .nestedScroll(rememberNestedScrollInteropConnection())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(sources.size) { idx ->
            val src = sources[idx]
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .clickable { onPick(src) },
                contentAlignment = Alignment.Center
            ) {
                when (src) {
                    is StickerSource.Png -> Image(
                        bitmap = ImageBitmap.imageResource(id = src.resId),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )

                    else -> {}
                }
            }
        }
    }
}
