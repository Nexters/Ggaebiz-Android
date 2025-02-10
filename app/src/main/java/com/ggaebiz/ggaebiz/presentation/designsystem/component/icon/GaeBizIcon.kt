package com.ggaebiz.ggaebiz.presentation.designsystem.component.icon

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.ggaebiz.ggaebiz.R

object GaeBizIcon {

    val icBack: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.ic_back)
    val icRightArrow: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.ic_right_arrow)

    val icPolygon: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.ic_polygon)

    val icColon : ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.icon_colon)
}
