package com.ggaebiz.ggaebiz.presentation.ui.statistic.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import com.ggaebiz.ggaebiz.presentation.ui.statistic.TopCardCase
import com.ggaebiz.ggaebiz.presentation.ui.statistic.TopCardState

private val CHAR_TOP_MARGIN = 40.dp
private val CHAR_SIZE = 180.dp
private val HEADER_END_PADDING = 120.dp
private val UNDERLINE_GAP = 8.dp

@Composable
fun StatisticTopCard(
    state: TopCardState,
    modifier: Modifier = Modifier,
) {
    val lineImage = ImageBitmap.imageResource(R.drawable.img_top_card_line)

    val nicknameStyle = GaeBizTheme.typography.titleSemiBold.copy(
        fontSize = 28.sp,
        lineHeight = 39.2.sp,
        letterSpacing = 0.sp,
    )
    val bodyStyle = GaeBizTheme.typography.bodySemiBold.copy(
        fontSize = 17.sp,
        lineHeight = 37.4.sp,
        letterSpacing = 0.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Top,
            trim = LineHeightStyle.Trim.None,
        ),
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
    ) {
        Image(
            painter = painterResource(R.drawable.img_top_card_bg),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize(),
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = CHAR_TOP_MARGIN, bottom = 24.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(CHAR_SIZE),
            ) {
                Image(
                    painter = painterResource(state.characterIconRes),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(CHAR_SIZE),
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(end = HEADER_END_PADDING),
                ) {
                    if (state.nickname.isNotEmpty()) {
                        Text(
                            text = state.nickname,
                            style = nicknameStyle,
                            color = GaeBizTheme.colors.white,
                        )
                    }
                    state.subtitleRes?.let { subtitleRes ->
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(R.drawable.ic_top_card_fire),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = formatText(subtitleRes, state.subtitleArg),
                                style = GaeBizTheme.typography.body2Medium,
                                color = GaeBizTheme.colors.white,
                            )
                        }
                    }
                }
            }

            state.bodyRes?.let { bodyRes ->
                WavyUnderlineText(
                    text = formatText(bodyRes, state.bodyArg),
                    lineImage = lineImage,
                    style = bodyStyle,
                    color = GaeBizTheme.colors.white,
                )
            }

            state.fromName?.let { fromName ->
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.statistic_top_card_from, fromName),
                    style = GaeBizTheme.typography.body2Medium,
                    color = GaeBizTheme.colors.white,
                    modifier = Modifier.align(Alignment.End),
                )
            }
        }
    }
}

@Composable
private fun formatText(resId: Int, arg: Int?): String =
    if (arg != null) stringResource(resId, arg) else stringResource(resId)

@Composable
private fun WavyUnderlineText(
    text: String,
    lineImage: ImageBitmap,
    style: TextStyle,
    color: Color,
    modifier: Modifier = Modifier,
) {
    var layout by remember { mutableStateOf<TextLayoutResult?>(null) }
    Text(
        text = text,
        style = style,
        color = color,
        onTextLayout = { layout = it },
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                val result = layout ?: return@drawBehind
                val fontPx = style.fontSize.value * density * fontScale
                val glyphBottomFromLineTop = fontPx * 1.15f
                for (line in 0 until result.lineCount) {
                    val left = result.getLineLeft(line)
                    val right = result.getLineRight(line)
                    val y = result.getLineTop(line) + glyphBottomFromLineTop + UNDERLINE_GAP.toPx()
                    tileLine(lineImage, left, right, y)
                }
            },
    )
}

private fun DrawScope.tileLine(image: ImageBitmap, startX: Float, endX: Float, y: Float) {
    val imageWidth = image.width
    val imageHeight = image.height
    if (imageWidth <= 0 || endX <= startX) return

    var x = startX
    while (x < endX) {
        val tileWidth = minOf(imageWidth.toFloat(), endX - x).toInt().coerceAtLeast(1)
        drawImage(
            image = image,
            srcOffset = IntOffset.Zero,
            srcSize = IntSize(tileWidth, imageHeight),
            dstOffset = IntOffset(x.toInt(), y.toInt()),
            dstSize = IntSize(tileWidth, imageHeight),
        )
        x += imageWidth
    }
}

@Preview(showBackground = true)
@Composable
private fun StatisticTopCardStreakPreview() {
    GaeBizTheme {
        StatisticTopCard(
            state = TopCardState(
                case = TopCardCase.STREAK,
                nickname = "깨비집사",
                bodyRes = R.string.statistic_top_card_streak_body,
                bodyArg = 12,
                characterIconRes = R.drawable.ic_positive_kiki,
                fromName = "키키",
            ),
            modifier = Modifier.padding(20.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StatisticTopCardReturnPreview() {
    GaeBizTheme {
        StatisticTopCard(
            state = TopCardState(
                case = TopCardCase.RETURN,
                nickname = "깨비집사",
                subtitleRes = R.string.statistic_top_card_return_subtitle,
                subtitleArg = 3,
                bodyRes = R.string.statistic_top_card_return_body,
                bodyArg = 3,
                characterIconRes = R.drawable.ic_positive_booboo,
                fromName = "부부",
            ),
            modifier = Modifier.padding(20.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StatisticTopCardEmptyPreview() {
    GaeBizTheme {
        StatisticTopCard(
            state = TopCardState(
                case = TopCardCase.FLOATING_EMPTY,
                nickname = "깨비집사",
                subtitleRes = R.string.statistic_top_card_floating_empty_subtitle,
                bodyRes = R.string.statistic_top_card_floating_empty_body,
                characterIconRes = R.drawable.ic_positive_kiki,
            ),
            modifier = Modifier.padding(20.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StatisticTopCardNewPreview() {
    GaeBizTheme {
        StatisticTopCard(
            state = TopCardState(
                case = TopCardCase.NEW,
                nickname = "깨비집사",
                subtitleRes = R.string.statistic_top_card_new_subtitle,
                bodyRes = R.string.statistic_top_card_new_body,
                characterIconRes = R.drawable.ic_positive_kiki,
                fromName = "키키",
            ),
            modifier = Modifier.padding(20.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StatisticTopCardFloatingActivePreview() {
    GaeBizTheme {
        StatisticTopCard(
            state = TopCardState(
                case = TopCardCase.FLOATING_ACTIVE,
                nickname = "깨비집사",
                subtitleRes = R.string.statistic_top_card_floating_active_subtitle,
                bodyRes = R.string.statistic_top_card_floating_active_body,
                bodyArg = 24,
                characterIconRes = R.drawable.ic_positive_nana,
                fromName = "나나",
            ),
            modifier = Modifier.padding(20.dp),
        )
    }
}
