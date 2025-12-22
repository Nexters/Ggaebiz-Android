package com.ggaebiz.ggaebiz.presentation.ui.proof.editor

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.common.extension.collectAsStateWithLifecycle
import com.ggaebiz.ggaebiz.presentation.common.extension.collectSideEffectWithLifecycle
import com.ggaebiz.ggaebiz.presentation.designsystem.component.header.GaeBizTextAppBar
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import com.ggaebiz.ggaebiz.presentation.ui.proof.Segmented2Tabs
import com.ggaebiz.ggaebiz.presentation.ui.proof.StickerPickerGrid
import com.ggaebiz.ggaebiz.presentation.ui.proof.StickersCanvas
import org.koin.androidx.compose.koinViewModel


@Composable
fun EditorScreen(
    viewModel: EditorViewModel = koinViewModel(),
    navigateBack: () -> Unit,
    navigateSave: (Uri) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.sideEffects.collectSideEffectWithLifecycle { effect ->
        when (effect) {
            EditorEffect.NavigateBack -> navigateBack()
            is EditorEffect.NavigateToSaveImage -> {
                navigateSave(effect.image)
            }
        }
    }

    uiState.imageUri?.let { image ->
        PhotoEditorScreen(
            imageUri = image,
            onBack = { navigateBack() },
            uiState = uiState,
            processIntent = viewModel::processIntent
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoEditorScreen(
    uiState: EditorState,
    imageUri: Uri,
    processIntent: (EditorIntent) -> Unit,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    LaunchedEffect(imageUri) {
        processIntent(EditorIntent.OnLoadImageData(context, imageUri))
    }

    val scaffoldState = rememberBottomSheetScaffoldState()
    var canvasSize by remember { mutableStateOf(IntSize(1, 1)) }
    var dragging by remember { mutableStateOf(false) }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        topBar = {
            GaeBizTextAppBar(
                titleRes = R.string.name_editor_title_text,
                iconOnClick = onBack,
                rightContent = {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(GaeBizTheme.colors.primaryOrange)
                            .clickable {
                                processIntent(EditorIntent.ClickFinish(canvasSize))
                            }
                            .padding(horizontal = 20.dp, vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "완성",
                            style = GaeBizTheme.typography.body2SemiBold,
                            color = GaeBizTheme.colors.white
                        )
                    }
                }
            )
        },
        sheetPeekHeight = 130.dp,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContainerColor = GaeBizTheme.colors.black44,
        sheetDragHandle = {
            BottomSheetDefaults.DragHandle(
                width = 56.dp,
                color = GaeBizTheme.colors.gray50
            )
        },
        sheetContent = {
            Segmented2Tabs(
                left = "타임 스탬프",
                right = "스티커",
                selectedRight = uiState.selectTab == SelectTab.STICKER,
                onSelectLeft = { processIntent(EditorIntent.ChangeTab(SelectTab.TIME_STAMP)) },
                onSelectRight = { processIntent(EditorIntent.ChangeTab(SelectTab.STICKER)) },
                modifier = Modifier.padding(top = 8.dp)
            )
            StickerPickerGrid(
                sources = uiState.nowSource,
                onPick = { src ->
                    processIntent(
                        EditorIntent.OnPick(
                            canvasSize = canvasSize,
                            source = src,
                            resource = context.resources
                        )
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.isImageLoadError -> {
                    Text("이미지를 불러오지 못했어요")
                }

                uiState.previewBitmap == null -> CircularProgressIndicator()
                else -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(3f / 4f)
                            .background(Color.Black, RoundedCornerShape(12.dp))
                            .onGloballyPositioned { canvasSize = it.size },
                    ) {
                        Image(
                            bitmap = uiState.previewBitmap,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        StickersCanvas(
                            canvasSize = canvasSize,
                            stickers = uiState.stickers.sortedBy { it.zIndex },
                            selectedId = uiState.selectImageId,
                            onSelect = { id -> processIntent(EditorIntent.OnSelectImage(id)) },
                            onBringToFront = { id ->
                                processIntent(EditorIntent.OnBringToFront(id))
                            },
                            onMove = { id, nx, ny ->
                                processIntent(EditorIntent.OnMove(id, nx, ny))
                            },
                            onRemove = { id ->
                                processIntent(EditorIntent.OnRemove(id))
                            },
                            onDragActiveChange = { dragging = it },  //  드래그 중 시트 스와이프 OFF,
                            onTransform = { id, pan, zoom, rotation ->
                                processIntent(EditorIntent.OnTransform(id,pan,zoom,rotation))
                            }
                        )
                    }
                }
            }
        }
    }
}


@Preview("인증샷", showBackground = true, apiLevel = 34)
@Composable
fun EditorScreenPreview() {
    GaeBizTheme {
        EditorScreen(
            navigateSave = {},
            navigateBack = {}
        )
    }
}