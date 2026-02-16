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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import com.ggaebiz.ggaebiz.presentation.model.Sticker
import com.ggaebiz.ggaebiz.presentation.model.StickerSource
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
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            (s.offset.x - sizePx / 2f).toInt(),
                            (s.offset.y - sizePx / 2f).toInt()
                        )
                    }
                    .size(sizeDp)
                    .graphicsLayer { rotationZ = s.rotation }
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
                when (s.source) {
                    is StickerSource.Png -> Image(
                        painter = painterResource(id = s.source.resId),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
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
                        painter = painterResource(id = src.resId),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}
