package com.ggaebiz.ggaebiz.presentation.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ggaebiz.ggaebiz.R


@Immutable
data class GaeBizTypography(
    val titleBold : TextStyle,
    val titleSemiBold : TextStyle,
    val bodyBold : TextStyle,
    val bodySemiBold : TextStyle,
    val bodyMedium : TextStyle,
    val body2Bold : TextStyle,
    val body2SemiBold : TextStyle,
    val body2Medium  : TextStyle,
    val label1 : TextStyle,
    val label2 : TextStyle,
    val label3 : TextStyle,
    val timer1 : TextStyle,
    val timer2 : TextStyle,
    val timer3 : TextStyle
)

val LocalGaeBizTypography = staticCompositionLocalOf {
    GaeBizTypography(
        titleBold = TextStyle.Default,
        titleSemiBold = TextStyle.Default,
        bodyBold = TextStyle.Default,
        bodySemiBold = TextStyle.Default,
        bodyMedium = TextStyle.Default,
        body2Bold = TextStyle.Default,
        body2SemiBold = TextStyle.Default,
        body2Medium = TextStyle.Default,
        label1 = TextStyle.Default,
        label2 = TextStyle.Default,
        label3 = TextStyle.Default,
        timer1 = TextStyle.Default,
        timer2 = TextStyle.Default,
        timer3 = TextStyle.Default,
    )
}

val GaeBizFont = FontFamily(
    Font(R.font.pretendard_medium, FontWeight.Medium),
    Font(R.font.pretendard_semibold, FontWeight.SemiBold),
    Font(R.font.pretendard_bold, FontWeight.Bold),
)

val Typography = GaeBizTypography(
    titleBold = TextStyle(
        fontFamily = GaeBizFont,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false,
        )
    ),
    titleSemiBold = TextStyle(
        fontFamily = GaeBizFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false,
        )
    ),
    bodyBold = TextStyle(
        fontFamily = GaeBizFont,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false,
        )
    ),
    bodySemiBold = TextStyle(
        fontFamily = GaeBizFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false,
        )
    ),
    bodyMedium = TextStyle(
        fontFamily = GaeBizFont,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false,
        )
    ),
    body2Bold = TextStyle(
        fontFamily = GaeBizFont,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false,
        )
    ),
    body2SemiBold = TextStyle(
        fontFamily = GaeBizFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false,
        )
    ),
    body2Medium = TextStyle(
        fontFamily = GaeBizFont,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false,
        )
    ),
    label1 = TextStyle(
        fontFamily = GaeBizFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false,
        )
    ),
    label2 = TextStyle(
        fontFamily = GaeBizFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false,
        )
    ),
    label3 =TextStyle(
        fontFamily = GaeBizFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false,
        )
    ),
    timer1 = TextStyle(
        fontFamily = GaeBizFont,
        fontWeight = FontWeight.Bold,
        fontSize = 80.sp,
        lineHeight = 84.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false,
        )
    ),
    timer2 = TextStyle(
        fontFamily = GaeBizFont,
        fontWeight = FontWeight.Bold,
        fontSize = 52.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false,
        )
    ),
    timer3 = TextStyle(
        fontFamily = GaeBizFont,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false,
        )
    )
)
