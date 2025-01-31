package com.ggaebiz.ggaebiz.presentation.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class GaeBizColors(
    val primaryOrange: Color,

    val white: Color,
    val white8: Color,
    val white12: Color,
    val white16: Color,

    val black: Color,
    val black8: Color,
    val black12: Color,
    val black16: Color,
    val black20: Color,
    val black24: Color,
    val black28: Color,
    val black32: Color,
    val black36: Color,
    val black40: Color,
    val black44: Color,

    val gray25: Color,
    val gray50: Color,
    val gray75: Color,
    val gray100: Color,
    val gray200: Color,
    val gray300: Color,
    val gray400: Color,
    val gray500: Color,
    val gray600: Color,
    val gray700: Color,
    val gray800: Color,
    val gray900: Color,
)

val LocalGaeBizColor = staticCompositionLocalOf {
    GaeBizColors(
        primaryOrange = Color.Unspecified,

        white = Color.Unspecified,
        white8 = Color.Unspecified,
        white12 = Color.Unspecified,
        white16 = Color.Unspecified,

        black = Color.Unspecified,
        black8 = Color.Unspecified,
        black12 = Color.Unspecified,
        black16 = Color.Unspecified,
        black20 = Color.Unspecified,
        black24 = Color.Unspecified,
        black28 = Color.Unspecified,
        black32 = Color.Unspecified,
        black36 = Color.Unspecified,
        black40 = Color.Unspecified,
        black44 = Color.Unspecified,

        gray25 = Color.Unspecified,
        gray50 = Color.Unspecified,
        gray75 = Color.Unspecified,
        gray100 = Color.Unspecified,
        gray200 = Color.Unspecified,
        gray300 = Color.Unspecified,
        gray400 = Color.Unspecified,
        gray500 = Color.Unspecified,
        gray600 = Color.Unspecified,
        gray700 = Color.Unspecified,
        gray800 = Color.Unspecified,
        gray900 = Color.Unspecified
    )
}

val GaeBizColorScheme = GaeBizColors(
    primaryOrange = Color(0xFFFC6F3D),
    white = Color(0xFFFFFFFF),
    white8 = Color(0x14FFFFFF),
    white12 = Color(0x1EFFFFFF),
    white16 = Color(0x28FFFFFF),

    black = Color(0xFF000000),
    black8 = Color(0x14000000),
    black12 = Color(0x1E000000),
    black16 = Color(0x28000000),
    black20 = Color(0x32000000),
    black24 = Color(0x3C000000),
    black28 = Color(0x46000000),
    black32 = Color(0x50000000),
    black36 = Color(0x5A000000),
    black40 = Color(0x64000000),
    black44 = Color(0x6E000000),

    gray25 = Color(0xFFF7F7F7),
    gray50 = Color(0xFFF1F1F2),
    gray75 = Color(0xFFE3E4E5),
    gray100 = Color(0xFFD3D4D6),
    gray200 = Color(0xFFBDBFC3),
    gray300 = Color(0xFFA0A2A8),
    gray400 = Color(0xFF888B94),
    gray500 = Color(0xFF70737C),
    gray600 = Color(0xFF666971),
    gray700 = Color(0xFF505258),
    gray800 = Color(0xFF3E3F44),
    gray900 = Color(0xFF2F3034)
)