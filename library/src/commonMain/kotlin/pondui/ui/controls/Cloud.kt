package pondui.ui.controls

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import pondui.ui.behavior.Magic
import pondui.ui.behavior.HotKey
import pondui.ui.behavior.clickableWithoutHoverEffect
import pondui.ui.nav.LocalPortal
import pondui.ui.theme.Pond
import pondui.ui.theme.ProvideBookColors

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Cloud(
    isVisible: Boolean,
    toggle: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    animationMillis: Int = 500,
    content: @Composable (() -> Unit) -> Unit,
) {
    val animation by animateFloatAsState(if (isVisible) 1f else 0f, animationSpec = tween(animationMillis))

    if (animation == 0f) return

    Dialog(
        onDismissRequest = toggle,
        properties = DialogProperties(
            scrimColor = Color.Transparent,
            usePlatformDefaultWidth = false
        )
    ) {
        Magic(isVisible, durationMillis = animationMillis) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
                    .background(Color.Black.copy(.5f))
                    .clickableWithoutHoverEffect(onClick = toggle)
            ) {
                title?.let {
                    Magic(isVisible, durationMillis = animationMillis, scale = .8f, offsetY = -(20.dp)) {
                        H1(title, modifier = Modifier.padding(Pond.ruler.unitPadding))
                    }
                }

                Magic(isVisible, durationMillis = animationMillis, scale = 1.2f, offsetY = 20.dp) {
                    ProvideBookColors {
                        Box(
                            modifier = modifier
                                .shadow(Pond.ruler.shadowElevation, shape = Pond.ruler.bigCorners)
                                .background(Pond.localColors.surface)
                                .padding(Pond.ruler.doublePadding)
                        ) {
                            content(toggle)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun rememberCloud(
    modifier: Modifier = Modifier,
    title: String? = null,
    content: @Composable (() -> Unit) -> Unit
): () -> Unit {
    var isVisible by remember { mutableStateOf(false) }

    val toggle = { isVisible = !isVisible }

    Cloud(
        isVisible = isVisible,
        toggle = toggle,
        title = title,
        content = content,
        modifier = modifier
    )

    return toggle
}