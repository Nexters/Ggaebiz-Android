package com.ggaebiz.ggaebiz.presentation.ui.statistic.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
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
import kotlin.math.roundToInt
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.data.model.CharacterName
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import com.ggaebiz.ggaebiz.presentation.ui.statistic.TopCardCase
import com.ggaebiz.ggaebiz.presentation.ui.statistic.TopCardState
import com.ggaebiz.ggaebiz.presentation.ui.statistic.iconRes
import com.ggaebiz.ggaebiz.presentation.ui.statistic.koreanName

private val CARD_SHAPE = RoundedCornerShape(24.dp)
private val CHAR_SIZE = 150.dp
private val HEADER_END_PADDING = 120.dp
private val HEADER_MIN_HEIGHT = 140.dp
private val UNDERLINE_GAP = 8.dp
private val UNDERLINE_HEIGHT = 6.dp

@Composable
fun StatisticTopCard(
    state: TopCardState,
    modifier: Modifier = Modifier,
) {
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
            .shadow(elevation = 3.dp, shape = CARD_SHAPE)
            .clip(CARD_SHAPE)
            .background(GaeBizTheme.colors.white),
    ) {
        Image(
            painter = painterResource(state.character.iconRes()),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 6.dp, end = 4.dp)
                .size(CHAR_SIZE),
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 28.dp, end = 28.dp, top = 28.dp, bottom = 20.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = HEADER_MIN_HEIGHT),
            ) {
                Column(modifier = Modifier.padding(end = HEADER_END_PADDING)) {
                    if (state.nickname.isNotEmpty()) {
                        Text(
                            text = stringResource(R.string.statistic_top_card_to, state.nickname),
                            style = nicknameStyle,
                            color = GaeBizTheme.colors.black,
                        )
                    }
                    state.subtitleRes?.let { subtitleRes ->
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(R.drawable.ic_top_card_fire),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(GaeBizTheme.colors.primaryOrange),
                                modifier = Modifier.size(18.dp),
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = formatText(subtitleRes, state.subtitleArg),
                                style = GaeBizTheme.typography.body2Medium,
                                color = GaeBizTheme.colors.black.copy(alpha = 0.7f),
                            )
                        }
                    }
                }
            }

            state.bodyRes?.let { bodyRes ->
                WavyUnderlineText(
                    text = formatText(bodyRes, state.bodyArg),
                    style = bodyStyle,
                    textColor = GaeBizTheme.colors.black,
                    waveColor = GaeBizTheme.colors.gray150,
                )
            }

            if (state.bodyRes != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.statistic_top_card_from, state.character.koreanName()),
                    style = GaeBizTheme.typography.body2Medium,
                    color = GaeBizTheme.colors.black.copy(alpha = 0.6f),
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
    style: TextStyle,
    textColor: Color,
    waveColor: Color,
    modifier: Modifier = Modifier,
) {
    val lineImage = ImageBitmap.imageResource(R.drawable.img_top_card_line)
    val tint = remember(waveColor) { ColorFilter.tint(waveColor) }
    var layout by remember { mutableStateOf<TextLayoutResult?>(null) }
    Text(
        text = text,
        style = style,
        color = textColor,
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
                    tileLine(lineImage, left, right, y, tint)
                }
            },
    )
}

private fun DrawScope.tileLine(
    image: ImageBitmap,
    left: Float,
    right: Float,
    centerY: Float,
    tint: ColorFilter,
) {
    val lineWidth = right - left
    if (lineWidth <= 0f) return
    val tileH = UNDERLINE_HEIGHT.toPx()
    val tileW = image.width * (tileH / image.height)
    if (tileW <= 0f || tileH <= 0f) return
    val top = (centerY - tileH / 2f).roundToInt()
    var x = left
    while (x < right) {
        val drawW = minOf(tileW, right - x)
        val srcW = (image.width * (drawW / tileW)).roundToInt().coerceIn(1, image.width)
        drawImage(
            image = image,
            srcOffset = IntOffset.Zero,
            srcSize = IntSize(srcW, image.height),
            dstOffset = IntOffset(x.roundToInt(), top),
            dstSize = IntSize(drawW.roundToInt(), tileH.roundToInt()),
            colorFilter = tint,
        )
        x += tileW
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
                character = CharacterName.KIKI,
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
                character = CharacterName.BOOBOO,
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
                character = CharacterName.KIKI,
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
                character = CharacterName.KIKI,
            ),
            modifier = Modifier.padding(20.dp),
        )
    }
}
