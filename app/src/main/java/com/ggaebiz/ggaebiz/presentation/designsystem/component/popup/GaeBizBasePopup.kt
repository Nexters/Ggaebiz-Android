import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme


@Immutable
enum class GaeBizPopupPosition { Center, Bottom }

@Immutable
enum class GaeBizButtonStyle { Primary, Secondary, Danger }

@Immutable
data class GaeBizPopupButton(
    val text: String,
    val onClick: () -> Unit,
    val style: GaeBizButtonStyle = GaeBizButtonStyle.Primary,
    val enabled: Boolean = true,
)

@Immutable
private data class PopupButtonPalette(
    val content: Color,
    val container: Color,
    val disabledContent: Color,
    val disabledContainer: Color,
    val radius: Int = 15,
)

@Composable
private fun popupButtonPalette(style: GaeBizButtonStyle): PopupButtonPalette = when (style) {
    GaeBizButtonStyle.Primary -> PopupButtonPalette(
        content = GaeBizTheme.colors.white,
        container = GaeBizTheme.colors.gray800,
        disabledContent = GaeBizTheme.colors.gray400,
        disabledContainer = GaeBizTheme.colors.gray200,
    )

    GaeBizButtonStyle.Secondary -> PopupButtonPalette(
        content = GaeBizTheme.colors.black,
        container = GaeBizTheme.colors.gray50,
        disabledContent = GaeBizTheme.colors.gray400,
        disabledContainer = GaeBizTheme.colors.gray100,
    )

    GaeBizButtonStyle.Danger -> PopupButtonPalette(
        content = GaeBizTheme.colors.white,
        container = GaeBizTheme.colors.red400,
        disabledContent = GaeBizTheme.colors.white,
        disabledContainer = GaeBizTheme.colors.red400.copy(alpha = 0.4f),
    )
}

@Composable
private fun GaeBizPopupButton(
    modifier: Modifier = Modifier,
    config: GaeBizPopupButton,
) {
    val palette = popupButtonPalette(config.style)
    val bg = if (config.enabled) palette.container else palette.disabledContainer
    val fg = if (config.enabled) palette.content else palette.disabledContent
    Box(
        modifier = modifier
            .defaultMinSize(minWidth = 0.dp)
            .heightIn(min = 56.dp)
            .clip(RoundedCornerShape(palette.radius.dp))
            .background(bg)
            .clickable(
                enabled = config.enabled,
                onClick = config.onClick
            )
            .padding(horizontal =20.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = config.text,
            style = GaeBizTheme.typography.bodySemiBold,
            color = fg,
            maxLines = 1,
            softWrap = false,
        )
    }
}

@Composable
fun GaeBizBasePopup(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    position: GaeBizPopupPosition = GaeBizPopupPosition.Center,
    containerColor: Color = GaeBizTheme.colors.white,
    shape: Shape = RoundedCornerShape(20.dp),
    maxWidth: Dp = 560.dp,
    contentPadding: PaddingValues = PaddingValues(20.dp),
    scrimColor: Color = GaeBizTheme.colors.black.copy(alpha = 0.45f),
    dismissOnScrim: Boolean = true,
    title: (@Composable () -> Unit),
    subtitle: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit = {},
    buttons: List<GaeBizPopupButton>? = emptyList(),
) {
    if (!visible) return
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(scrimColor)
            .let { m -> if (dismissOnScrim) m.clickable { onDismissRequest() } else m }
    ) {
        val align = when (position) {
            GaeBizPopupPosition.Center -> Alignment.Center
            GaeBizPopupPosition.Bottom -> Alignment.BottomCenter
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 16.dp,
                    vertical = if (position == GaeBizPopupPosition.Bottom) 16.dp else 0.dp
                ),
            contentAlignment = align
        ) {
            Box(
                modifier = modifier
                    .fillMaxWidth(0.912f)
                    .widthIn(max = maxWidth)
                    .clip(shape)
                    .background(containerColor)
                    .clickable(enabled = false) {}
            ) {
                Column(
                    modifier = Modifier
                        .padding(contentPadding)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.Start
                ) {
                    Spacer(Modifier.height(24.dp))
                    title()
                    if (subtitle != null) {
                        Spacer(Modifier.height(4.dp))
                        subtitle()
                    }
                    Spacer(Modifier.height(24.dp))
                    content()
                    buttons?.let {
                        if (it.isNotEmpty()){
                            Spacer(Modifier.height(24.dp))
                            ButtonsSection(buttons = buttons)
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun ButtonsSection(buttons: List<GaeBizPopupButton>) {
    require(buttons.size <= 2) { "GaeBizBasePopup supports up to 2 buttons." }
    when (buttons.size) {
        0 -> Unit
        1 -> {
            GaeBizPopupButton(
                modifier = Modifier.fillMaxWidth(),
                config = buttons[0]
            )
        }

        2 -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                buttons.forEach { cfg ->
                    GaeBizPopupButton(
                        modifier = Modifier.weight(1f),
                        config = cfg
                    )
                }
            }
        }

        else -> Unit
    }
}

