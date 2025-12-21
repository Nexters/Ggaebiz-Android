package com.ggaebiz.ggaebiz.presentation.ui.proof.finish

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.common.extension.collectAsStateWithLifecycle
import com.ggaebiz.ggaebiz.presentation.common.extension.collectSideEffectWithLifecycle
import com.ggaebiz.ggaebiz.presentation.designsystem.component.button.GaeBizButton
import com.ggaebiz.ggaebiz.presentation.designsystem.component.header.GaeBizTextAppBar
import com.ggaebiz.ggaebiz.presentation.designsystem.component.icon.GaeBizIcon
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.PretendardFont
import org.koin.androidx.compose.koinViewModel

@Composable
fun EditorResultScreen(
    viewModel: EditorResultViewModel = koinViewModel(),
    navigateBack: () -> Unit,
    navigateHome: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        uiState.imageUri?.let { image ->
            viewModel.processIntent(EditorResultIntent.EnterScreen(context, image))
        }
    }
    viewModel.sideEffects.collectSideEffectWithLifecycle { effect ->
        when (effect) {
            EditorResultSideEffect.NavigateBack -> navigateBack()
            EditorResultSideEffect.MoveToDeviceSetting -> {
                val intent = Intent()
                intent.action = Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
                context.startActivity(intent)
            }

            is EditorResultSideEffect.ShowToast -> {
                Toast.makeText(context, effect.msg, Toast.LENGTH_SHORT).show()
            }

            is EditorResultSideEffect.ShareImage -> {
                openSystemShare(context, effect.uri)
            }
        }
    }

    ResultContent(
        uiState = uiState,
        onBack = navigateBack,
        onClose = navigateHome,
        processIntent = viewModel::processIntent
    )
}

@Composable
fun ResultContent(
    uiState: EditorResultState,
    onBack: () -> Unit,
    onClose: () -> Unit,
    processIntent: (EditorResultIntent) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        GaeBizTextAppBar(
            titleRes = R.string.name_editor_title_text,
            iconOnClick = onBack,
            rightContent = {
                Icon(
                    imageVector = GaeBizIcon.icCloseScreen,
                    tint = GaeBizTheme.colors.black,
                    contentDescription = null,
                    modifier = Modifier
                        .clickable { onClose() }
                        .padding(horizontal = 16.dp)
                )
            }
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                text = "내 기록을 친구에게\n자랑해볼까요?",
                style = TextStyle(
                    fontFamily = PretendardFont,
                    fontWeight = FontWeight(700),
                    fontSize = 24.sp,
                    lineHeight = 36.sp,
                    letterSpacing = 0.5.sp
                ),
                color = GaeBizTheme.colors.gray900,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            when {
                uiState.isImageLoadError -> {
                    Text("이미지를 불러오지 못했어요")
                }

                uiState.imageBitmap == null -> CircularProgressIndicator()
                else -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(3f / 4f)
                            .background(Color.Black, RoundedCornerShape(12.dp))
                    ) {
                        Image(
                            bitmap = uiState.imageBitmap,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    processIntent(EditorResultIntent.ClickSaveImage)
                }
                .padding(vertical = 12.dp, horizontal = 28.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "이미지 저장",
                style = GaeBizTheme.typography.bodySemiBold,
                color = GaeBizTheme.colors.black
            )
            Spacer(Modifier.width(4.dp))
            Icon(
                imageVector = GaeBizIcon.icDownLoadImage,
                tint = GaeBizTheme.colors.black,
                contentDescription = null,
            )
        }
        GaeBizButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            onClick = { processIntent(EditorResultIntent.ClickShareImage) },
            contentColor = GaeBizTheme.colors.white,
            containerColor = GaeBizTheme.colors.gray800,
            text = "SNS 공유",
            style = GaeBizTheme.typography.bodySemiBold,
        )
    }
}

private fun openSystemShare(
    context: Context,
    uri: Uri,
) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/*"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(
        Intent.createChooser(intent, "이미지 공유")
    )
}


@Preview(showBackground = true)
@Composable
fun ConfigScreenPreview() {
    GaeBizTheme {
        EditorResultScreen(
            navigateHome = {},
            navigateBack = {}
        )
    }
}