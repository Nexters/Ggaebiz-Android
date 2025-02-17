package com.ggaebiz.ggaebiz.presentation.designsystem.ui

import android.annotation.SuppressLint
import android.graphics.Insets
import android.os.Build
import android.view.WindowInsets
import android.view.WindowManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme

@Composable
fun FullScreen(
    modifier: Modifier = Modifier,
    backGroundImage: Int? = null,
    backGroundGradient: List<Color>? = null,
    isSplash: Boolean = false,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .then(if (isSplash) Modifier.offset(y = -rememberStatusBarHeight()) else Modifier)
            .background(GaeBizTheme.colors.primaryOrange)
            .fillMaxSize(),
    ) {
        if (backGroundImage != null) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(backGroundImage),
                contentDescription = "",
                contentScale = ContentScale.FillWidth,
            )
        }
        if (backGroundGradient != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = backGroundGradient,
                        ),
                    ),
            )
        }
        content()
    }
}


@SuppressLint("InternalInsetResource")
@Composable
fun rememberStatusBarHeight(): Dp {
    val context = LocalContext.current
    val density = LocalDensity.current

    return remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics =
                context.getSystemService(WindowManager::class.java)?.currentWindowMetrics
            val insets =
                windowMetrics?.windowInsets?.getInsetsIgnoringVisibility(WindowInsets.Type.statusBars())
                    ?: Insets.of(0, 0, 0, 0)
            with(density) { insets.top.toDp() }
        } else {
            val resourceId =
                context.resources.getIdentifier("status_bar_height", "dimen", "android")
            val heightPx =
                if (resourceId > 0) context.resources.getDimensionPixelSize(resourceId) else 0
            with(density) { heightPx.toDp() }
        }
    }
}
