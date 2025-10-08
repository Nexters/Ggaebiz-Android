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

    val icBelowPolygon: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.ic_polygon)

    val icAbovePolygon: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.ic_above_polygon)


    val icColon : ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.icon_colon)

    val icOrangeSpeaker : ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.ic_orange_speaker)

    val icFillCheck : ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.icon_fill_check)

    val icProofCamera : ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.icon_proof_camera)

    val icProofAlbum : ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.icon_proof_album)

    val icProofCard : ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.icon_proof_card)
}
