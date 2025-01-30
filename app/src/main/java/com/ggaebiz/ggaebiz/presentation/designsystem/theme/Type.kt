 package com.ggaebiz.ggaebiz.presentation.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ggaebiz.ggaebiz.R

 val GaeBizFont = FontFamily(
     Font(R.font.pretendard_medium, FontWeight.Medium),
     Font(R.font.pretendard_semibold, FontWeight.SemiBold),
     Font(R.font.pretendard_bold, FontWeight.Bold),
 )

 /**
  * titleLarge: 상단바 제목 텍스트
  *
  * bodyLarge: 버튼 텍스트
  * bodyMedium: 선택된 레벨 / 시간, 분
  * bodySmall: 미선택 레벨
  *
  * labelLarge: 캐릭터 말풍선
  * labelMedium: 캐릭터 이름
  * labelSmall: 캐릭터 특성
  * 
  * headlineLarge: 큰 타이머
  * headlineMedium: 중간 타이머
  * headlineSmall: 작은 타이머
  */
 val GaeBizTypography = Typography(
     titleLarge = TextStyle(
         fontFamily = GaeBizFont,
         fontWeight = FontWeight.SemiBold,
         fontSize = 20.sp,
         lineHeight = 24.sp,
     ),

     bodyLarge = TextStyle(
         fontFamily = GaeBizFont,
         fontWeight = FontWeight.Bold,
         fontSize = 16.sp,
         lineHeight = 19.09.sp,
     ),
     bodyMedium = TextStyle(
         fontFamily = GaeBizFont,
         fontWeight = FontWeight.SemiBold,
         fontSize = 14.sp,
         lineHeight = 16.71.sp,
     ),
     bodySmall = TextStyle(
         fontFamily = GaeBizFont,
         fontWeight = FontWeight.SemiBold,
         fontSize = 12.sp,
         lineHeight = 14.32.sp,
     ),

     labelLarge = TextStyle(
         fontFamily = GaeBizFont,
         fontWeight = FontWeight.SemiBold,
         fontSize = 16.sp,
         lineHeight = 24.sp,
     ),
     labelMedium = TextStyle(
         fontFamily = GaeBizFont,
         fontWeight = FontWeight.Bold,
         fontSize = 20.sp,
         lineHeight = 20.sp,
     ),
     labelSmall = TextStyle(
         fontFamily = GaeBizFont,
         fontWeight = FontWeight.SemiBold,
         fontSize = 16.sp,
         lineHeight = 20.sp,
     ),

     headlineLarge = TextStyle(
         fontFamily = GaeBizFont,
         fontWeight = FontWeight.Bold,
         fontSize = 80.sp,
         lineHeight = 84.sp,
         letterSpacing = 4.sp,
     ),
     headlineMedium = TextStyle(
         fontFamily = GaeBizFont,
         fontWeight = FontWeight.SemiBold,
         fontSize = 52.sp,
         lineHeight = 57.2.sp,
     ),
     headlineSmall = TextStyle(
         fontFamily = GaeBizFont,
         fontWeight = FontWeight.Medium,
         fontSize = 40.sp,
         lineHeight = 44.sp,
     ),
 )
