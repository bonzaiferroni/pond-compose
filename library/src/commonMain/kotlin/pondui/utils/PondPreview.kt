package pondui.utils

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pondui.ui.controls.*
import pondui.ui.modifiers.artBackground
import pondui.ui.nav.LocalPortal
import pondui.ui.nav.PortalModel
import pondui.ui.theme.*

@Composable
fun PondPreview(content: @Composable () -> Unit) {
    ProvideTheme {
        ProvidePondLocals {
            content()
        }
    }
}

@Composable
fun ProvidePondLocals(content: @Composable () -> Unit) {
    val portal = remember { PortalModel() }
    CompositionLocalProvider(LocalPortal provides portal) {
        content()
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

@Composable
fun MultiThemePreview(
    vararg themes: Pair<String, PondTheme>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.DarkGray)
            .padding(4.dp)
    ) {
        themes.forEach { (themeLabel, theme) ->
            ProvideTheme(theme) {
                ProvidePondLocals {
                    PreviewFrame(themeLabel, "Text") {
                        FlowRow(1, itemVerticalAlignment = Alignment.Bottom) {
                            Text("Text")
                            Label("Label")
                            Text("Bold", style = Pond.typo.bold)
                            Text("Italic", style = Pond.typo.italic)
                            Text("Light", style = Pond.typo.light)
                        }
                        FlowRow(1, itemVerticalAlignment = Alignment.Bottom) {
                            H1("Heading 1")
                            H2("Heading 2")
                            H3("Heading 3")
                            H4("Heading 4")
                            H5("Heading 5")
                        }
                        FlowRow(1) {
                            Button("Accent") { }
                            Button("Primary", color = Pond.colors.primary) { }
                        }
                    }
                }
            }
        }
    }
}