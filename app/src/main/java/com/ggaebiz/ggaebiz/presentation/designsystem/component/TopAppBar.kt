package com.ggaebiz.ggaebiz.presentation.designsystem.component

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.designsystem.icon.GgaebizIcon
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GgabizTypography
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.Gray800

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GgaebizTopAppBar(
    modifier: Modifier = Modifier,
    @StringRes titleRes: Int? = null,
    titleDrawable: Int? = null,
    navigationIcon: @Composable () -> Unit = {},
    actionIcon: ImageVector? = null,
    actionIconContentDescription: String? = null,
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
    onActionClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        modifier = modifier.wrapContentSize(align = Alignment.Center),
        colors = colors,
        title = {
            when {
                titleRes != null -> {
                    Text(
                        text = stringResource(id = titleRes),
                        style = GgabizTypography.titleLarge,
                        color = Gray800
                    )
                }

                titleDrawable != null -> {
                    Image(
                        painter = painterResource(id = titleDrawable),
                        contentDescription = "Title Image",
                        modifier = modifier
                            .height(20.dp)
                            .width(70.dp),
                    )
                }
            }
        },
        navigationIcon = {
            Box(
                modifier = modifier.padding(
                    horizontal = 16.dp,
                    vertical = 12.dp,
                ),
            ) {
                navigationIcon()
            }
        },
        actions = {
            if (actionIcon != null && actionIconContentDescription != null) {
                IconButton(onClick = onActionClick) {
                    Icon(
                        imageVector = actionIcon,
                        contentDescription = actionIconContentDescription,
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview("Image App Bar")
@Composable
private fun GgaebizImageTopAppBarPreview() {
    GgaebizTopAppBar(
        titleDrawable = R.drawable.ggaebiz_kor,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview("Text App Bar")
@Composable
private fun GgaebizTextTopAppBarPreview() {
    GgaebizTopAppBar(
        titleRes = R.string.setting_title,
        navigationIcon = {
            GgaebizIconButton(
                onClick = {},
                icon = {
                    Icon(
                        painter = GgaebizIcon.icBack(),
                        contentDescription = null,
                    )
                },
            )
        },
    )
}
