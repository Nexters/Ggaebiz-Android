package com.ggaebiz.ggaebiz.presentation.ui.config

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.designsystem.component.icon.GaeBizIcon
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.PretendardFont

@Composable
fun NicknameEditDialog(
    nickname: String,
    error: NicknameError,
    canSave: Boolean,
    onValueChange: (String) -> Unit,
    onClickRandom: () -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboard?.show()
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(GaeBizTheme.colors.black.copy(alpha = 0.4f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss,
                ),
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .imePadding()
                    .padding(start = 16.dp, end = 16.dp, bottom = 20.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(GaeBizTheme.colors.white)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {},
                    )
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 28.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                DialogHandle()
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                ) {
                    Text(
                        text = stringResource(R.string.nickname_edit_title),
                        style = GaeBizTheme.typography.titleSemiBold,
                        color = GaeBizTheme.colors.gray900,
                    )
                    NicknameInputField(
                        nickname = nickname,
                        error = error,
                        focusRequester = focusRequester,
                        onValueChange = onValueChange,
                        onClickRandom = onClickRandom,
                    )
                }
                NicknameDialogButtons(
                    canSave = canSave,
                    onSave = onSave,
                    onDismiss = onDismiss,
                )
            }
        }
    }
}

@Composable
private fun DialogHandle() {
    Box(
        modifier = Modifier
            .size(width = 56.dp, height = 4.dp)
            .background(GaeBizTheme.colors.gray100, RoundedCornerShape(4.dp)),
    )
}

@Composable
private fun NicknameInputField(
    nickname: String,
    error: NicknameError,
    focusRequester: FocusRequester,
    onValueChange: (String) -> Unit,
    onClickRandom: () -> Unit,
) {
    val underlineColor = if (error == NicknameError.Special) {
        GaeBizTheme.colors.red600
    } else {
        GaeBizTheme.colors.gray100
    }
    val counterColor = if (error == NicknameError.None) {
        GaeBizTheme.colors.gray300
    } else {
        GaeBizTheme.colors.red600
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .drawBehind {
                    val strokeWidth = 1.dp.toPx()
                    drawLine(
                        color = underlineColor,
                        start = Offset(0f, size.height - strokeWidth / 2),
                        end = Offset(size.width, size.height - strokeWidth / 2),
                        strokeWidth = strokeWidth,
                    )
                }
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            BasicTextField(
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
                value = nickname,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = TextStyle(
                    fontFamily = PretendardFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                    color = GaeBizTheme.colors.gray900,
                ),
                cursorBrush = SolidColor(GaeBizTheme.colors.gray900),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(),
            )
            RandomButton(onClick = onClickRandom)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (error != NicknameError.None) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = GaeBizIcon.icErrorWarning,
                    contentDescription = null,
                    tint = GaeBizTheme.colors.red600,
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = stringResource(
                        if (error == NicknameError.Special) {
                            R.string.nickname_error_special
                        } else {
                            R.string.nickname_error_length
                        }
                    ),
                    style = CounterTextStyle,
                    color = GaeBizTheme.colors.red600,
                )
            }
            Spacer(Modifier.weight(1f))
            Text(
                text = stringResource(
                    R.string.nickname_counter,
                    nickname.length.coerceAtMost(ConfigViewModel.MAX_NICKNAME_LENGTH),
                ),
                style = CounterTextStyle,
                color = counterColor,
            )
        }
    }
}

@Composable
private fun RandomButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .height(32.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .background(GaeBizTheme.colors.gray50)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            text = stringResource(R.string.nickname_random_btn),
            style = GaeBizTheme.typography.label3,
            color = GaeBizTheme.colors.black,
        )
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = GaeBizIcon.icRefresh,
            contentDescription = null,
            tint = GaeBizTheme.colors.black,
        )
    }
}

@Composable
private fun NicknameDialogButtons(
    canSave: Boolean,
    onSave: () -> Unit,
    onDismiss: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        DialogButton(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.nickname_close_btn),
            containerColor = GaeBizTheme.colors.gray50,
            contentColor = GaeBizTheme.colors.black,
            enabled = true,
            onClick = onDismiss,
        )
        DialogButton(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.nickname_save_btn),
            containerColor = GaeBizTheme.colors.gray800,
            contentColor = GaeBizTheme.colors.white,
            disabledContainerColor = GaeBizTheme.colors.gray100,
            disabledContentColor = GaeBizTheme.colors.gray400,
            enabled = canSave,
            onClick = onSave,
        )
    }
}

@Composable
private fun DialogButton(
    text: String,
    containerColor: Color,
    contentColor: Color,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    disabledContainerColor: Color = containerColor,
    disabledContentColor: Color = contentColor,
) {
    Box(
        modifier = modifier
            .height(56.dp)
            .clip(RoundedCornerShape(15.dp))
            .clickable(enabled = enabled) { onClick() }
            .background(if (enabled) containerColor else disabledContainerColor),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = GaeBizTheme.typography.bodySemiBold,
            color = if (enabled) contentColor else disabledContentColor,
        )
    }
}

private val CounterTextStyle = TextStyle(
    fontFamily = PretendardFont,
    fontWeight = FontWeight.Medium,
    fontSize = 12.sp,
)
