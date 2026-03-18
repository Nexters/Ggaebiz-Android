package com.ggaebiz.ggaebiz.presentation.ui.proof.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.designsystem.component.button.GaeBizButton
import com.ggaebiz.ggaebiz.presentation.designsystem.component.icon.GaeBizIcon
import com.ggaebiz.ggaebiz.presentation.designsystem.component.timer.TimerLargeColon
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme

@Composable
fun ProofCardBottomButton(
    isSaveMode : Boolean,
    onCreateAndSaveImage: () -> Unit,
    onCreateAndShareImage: () -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (isSaveMode) 0f else 1f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = !isSaveMode) {
                    onCreateAndSaveImage()
                }
                .padding(vertical = 12.dp, horizontal = 28.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.proof_card_save_image_text),
                style = GaeBizTheme.typography.bodySemiBold,
                color = GaeBizTheme.colors.black
            )
            Spacer(Modifier.width(4.dp))
            Icon(
                imageVector = GaeBizIcon.icDownLoadImage,
                tint = GaeBizTheme.colors.black,
                contentDescription = null
            )
        }
        GaeBizButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 4.dp
                ),
            onClick = { onCreateAndShareImage() },
            contentColor = GaeBizTheme.colors.white,
            containerColor = GaeBizTheme.colors.gray800,
            text = stringResource(R.string.proof_card_share_image_text),
            style = GaeBizTheme.typography.bodySemiBold
        )
    }
}


@Composable
fun ProofCardImage(
    characterImageResId: Int,
    hour: Int,
    minute: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(width = 279.dp, height = 400.dp)
            .clip(RoundedCornerShape(20.dp))
    ) {
        Image(
            painter = painterResource(id = characterImageResId),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillHeight
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 30.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            TimerRow(
                hour = hour,
                minute = minute
            )
        }
    }
}


@Composable
private fun TimerRow(
    hour: Int,
    minute: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = String.format("%02d", hour),
            style = GaeBizTheme.typography.timer1,
            color = GaeBizTheme.colors.white
        )
        TimerLargeColon(isBlack = false)
        Text(
            text = String.format("%02d", minute),
            style = GaeBizTheme.typography.timer1,
            color = GaeBizTheme.colors.white
        )
    }
}


@Composable
fun SaveGuideComponent() {
    Row(
        modifier = Modifier
            .wrapContentWidth()
            .background(
                GaeBizTheme.colors.white,
                RoundedCornerShape(40.dp)
            )
            .padding(12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = GaeBizIcon.icSaveImageFinger,
            tint = Color.Unspecified,
            contentDescription = null,
            modifier = Modifier.padding(end = 9.dp)
        )
        Text(
            text = stringResource(R.string.proof_card_save_guide_text),
            style = GaeBizTheme.typography.bodyBold,
            color = GaeBizTheme.colors.black
        )
    }
}
