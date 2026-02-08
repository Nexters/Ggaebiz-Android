package com.ggaebiz.ggaebiz.presentation.ui.proof.card

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.common.extension.collectAsStateWithLifecycle
import com.ggaebiz.ggaebiz.presentation.common.extension.collectSideEffectWithLifecycle
import com.ggaebiz.ggaebiz.presentation.designsystem.component.header.GaeBizTextAppBar
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.PretendardFont
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProofCardScreen(
    viewModel: ProofCardViewModel = koinViewModel(),
    navigateBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.processIntent(ProofCardIntent.EnterScreen(context))
    }

    LaunchedEffect(uiState.characterImageResId, uiState.hour, uiState.minute) {
        if (uiState.characterImageResId != 0 && uiState.previewBitmap == null) {
            scope.launch {
                val bitmap = createProofCardBitmap(
                    context = context,
                    characterImageResId = uiState.characterImageResId,
                    hour = uiState.hour,
                    minute = uiState.minute
                )
                bitmap?.let {
                    viewModel.processIntent(ProofCardIntent.CreateProofCardBitmap(it))
                }
            }
        }
    }

    viewModel.sideEffects.collectSideEffectWithLifecycle { effect ->
        when (effect) {
            ProofCardSideEffect.NavigateBack -> navigateBack()
            is ProofCardSideEffect.ShowToast -> {
                Toast.makeText(context, context.getString(effect.msgResId), Toast.LENGTH_SHORT).show()
            }

            is ProofCardSideEffect.ShareImage -> {
                openSystemShare(context, effect.uri)
            }

            ProofCardSideEffect.SaveImageSuccess -> {
                Toast.makeText(context, context.getString(R.string.proof_card_save_success_text), Toast.LENGTH_SHORT).show()
            }

            is ProofCardSideEffect.SaveImageFailure -> {
                Toast.makeText(context, context.getString(effect.msgResId), Toast.LENGTH_SHORT).show()
            }
        }
    }

    ProofCardContent(
        uiState = uiState,
        onBack = {
            if (uiState.isSaveMode) {
                viewModel.processIntent(ProofCardIntent.ClickBack)
            } else {
                navigateBack()
            }
        },
        onCreateAndSaveImage = { viewModel.processIntent(ProofCardIntent.ClickSaveImage) },
        onCreateAndShareImage = { viewModel.processIntent(ProofCardIntent.ClickShareImage) },
        onLongPressImage = { viewModel.processIntent(ProofCardIntent.LongPressImage) }
    )
}

@Composable
fun ProofCardContent(
    uiState: ProofCardState,
    onBack: () -> Unit,
    onCreateAndSaveImage: () -> Unit,
    onCreateAndShareImage: () -> Unit,
    onLongPressImage: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        GaeBizTheme.colors.gray25,
                        GaeBizTheme.colors.primaryOrange100
                    )
                )
            )
    ) {
        GaeBizTextAppBar(
            titleRes = R.string.name_editor_title_text,
            iconOnClick = onBack
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 22.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(if (uiState.isSaveMode) 0f else 1f),
                text = stringResource(R.string.proof_card_title_text),
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
            Spacer(Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .pointerInput(uiState.isSaveMode) {
                        detectTapGestures(
                            onLongPress = { onLongPressImage() }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                if (uiState.characterImageResId != 0) {
                    ProofCardImage(
                        characterImageResId = uiState.characterImageResId,
                        hour = uiState.hour,
                        minute = uiState.minute,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            Box(
                modifier = Modifier.fillMaxWidth().alpha(if (uiState.isSaveMode) 1f else 0f),
                contentAlignment = Alignment.Center
            ) {
                SaveGuideComponent()
            }
        }
        ProofCardBottomButton(
            uiState.isSaveMode,
            onCreateAndSaveImage,
            onCreateAndShareImage
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
        Intent.createChooser(intent, context.getString(R.string.proof_card_share_chooser_title))
    )
}


/**
 * ComposeView를 사용하여 ProofCard Bitmap 생성
 */
private suspend fun createProofCardBitmap(
    context: Context,
    characterImageResId: Int,
    hour: Int,
    minute: Int,
): ImageBitmap? = withContext(Dispatchers.Main) {
    val activity = context as? Activity ?: return@withContext null
    val cardWidthDp = 279.dp
    val cardHeightDp = 400.dp
    val density = context.resources.displayMetrics.density

    // 🔹 실제 Bitmap px (2배 해상도)
    val bitmapWidthPx = (279 * density * 2).toInt()
    val bitmapHeightPx = (400 * density * 2).toInt()

    val composeView = ComposeView(activity).apply {
        visibility = View.INVISIBLE
        setContent {
            GaeBizTheme {
                ProofCardImage(
                    characterImageResId = characterImageResId,
                    hour = hour,
                    minute = minute,
                    modifier = Modifier
                        .width(cardWidthDp)
                        .height(cardHeightDp)
                )
            }
        }
    }

    val rootView = activity.findViewById<ViewGroup>(android.R.id.content)
    rootView.addView(composeView)
    try {
        composeView.measure(
            View.MeasureSpec.makeMeasureSpec(
                (279 * density).toInt(),
                View.MeasureSpec.EXACTLY
            ),
            View.MeasureSpec.makeMeasureSpec(
                (400 * density).toInt(),
                View.MeasureSpec.EXACTLY
            )
        )
        composeView.layout(
            0,
            0,
            composeView.measuredWidth,
            composeView.measuredHeight
        )
        val bitmap = Bitmap.createBitmap(
            bitmapWidthPx,
            bitmapHeightPx,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        canvas.scale(2f, 2f)
        composeView.draw(canvas)

        bitmap.asImageBitmap()
    } finally {
        rootView.removeView(composeView)
    }
}

@Preview(showBackground = true)
@Composable
fun ProofCardScreenPreview() {
    GaeBizTheme {
        ProofCardScreen(
            navigateBack = {}
        )
    }
}

