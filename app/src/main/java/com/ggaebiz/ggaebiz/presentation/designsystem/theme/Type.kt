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
    val titleBold: TextStyle,
    val titleSemiBold: TextStyle,
    val bodyBold: TextStyle,
    val bodySemiBold: TextStyle,
    val bodyMedium: TextStyle,
    val body2Bold: TextStyle,
    val body2SemiBold: TextStyle,
    val body2Medium: TextStyle,
    val label1: TextStyle,
    val label2: TextStyle,
    val label3: TextStyle,
    val timer1: TextStyle,
    val timer2: TextStyle,
    val timer3: TextStyle,
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

val PretendardFont = FontFamily(
    Font(R.font.pretendard_medium, FontWeight.Medium),
    Font(R.font.pretendard_semibold, FontWeight.SemiBold),
    Font(R.font.pretendard_bold, FontWeight.Bold),
)

val ZuumeFont = FontFamily(
    Font(R.font.zuume_medium, FontWeight.Medium),
    Font(R.font.zuume_semibold, FontWeight.SemiBold),
    Font(R.font.zuume_bold, FontWeight.Bold)
)

val Typography = GaeBizTypography(
    titleBold = TextStyle(
        fontFamily = PretendardFont,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 24.sp
    ),
    titleSemiBold = TextStyle(
        fontFamily = PretendardFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 24.sp
    ),
    bodyBold = TextStyle(
        fontFamily = PretendardFont,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    bodySemiBold = TextStyle(
        fontFamily = PretendardFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = PretendardFont,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    body2Bold = TextStyle(
        fontFamily = PretendardFont,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    body2SemiBold = TextStyle(
        fontFamily = PretendardFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    body2Medium = TextStyle(
        fontFamily = PretendardFont,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    label1 = TextStyle(
        fontFamily = PretendardFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 20.sp
    ),
    label2 = TextStyle(
        fontFamily = PretendardFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp
    ),
    label3 = TextStyle(
        fontFamily = PretendardFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp
    ),
    timer1 = TextStyle(
        fontFamily = ZuumeFont,
        fontWeight = FontWeight.Bold,
        fontSize = 80.sp,
        lineHeight = 84.sp
    ),
    timer2 = TextStyle(
        fontFamily = ZuumeFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 52.sp
    ),
    timer3 = TextStyle(
        fontFamily = ZuumeFont,
        fontWeight = FontWeight.Medium,
        fontSize = 40.sp
    )
)
