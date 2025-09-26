package pondui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pondui.ui.controls.Column
import pondui.ui.controls.Label
import pondui.ui.modifiers.artBackground
import pondui.ui.nav.LocalPortal
import pondui.ui.nav.PortalModel
import pondui.ui.theme.Pond
import pondui.ui.theme.ProvideTheme

@Composable
fun PondPreview(content: @Composable () -> Unit) {
    ProvideTheme {
        val portal = remember { PortalModel() }
        CompositionLocalProvider(LocalPortal provides portal) {
            content()
        }
    }
}

@Composable
fun PreviewFrame(
    title: String? = null,
    subtitle: String? = null,
    content: @Composable () -> Unit
) {
    Column(1, horizontalAlignment = Alignment.CenterHorizontally) {
        if (title != null || subtitle != null) {
            Box(modifier = Modifier.fillMaxWidth()) {
                title?.let {
                    Label(
                        title,
                        modifier = Modifier.align(if (subtitle != null) Alignment.CenterStart else Alignment.Center)
                    ) }
                subtitle?.let {
                    Label(
                        subtitle,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }
        }
        Box(
            modifier = Modifier.shadow(2.dp)
                .border(1.dp, Color.Black)
        ) {
            Column(
                gap = 1,
                modifier = Modifier
                    .background(Pond.colors.background)
                    .artBackground()
                    .padding(Pond.ruler.unitPadding)
            ) {
                content()
            }
        }
    }
}

@Composable
fun SinglePreview(
    title: String? = null,
    content: @Composable () -> Unit
) {
    PondPreview {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
                .background(Color.DarkGray)
                .padding(Pond.ruler.unitPadding)
        ) {
            PreviewFrame(title) {
                content()
            }
        }
    }
}

@Composable
fun MultiPreview(content: @Composable () -> Unit) {
    PondPreview {
        Column(2,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
                .background(Color.DarkGray)
                .padding(Pond.ruler.unitPadding)
        ) {
            content()
        }
    }
}