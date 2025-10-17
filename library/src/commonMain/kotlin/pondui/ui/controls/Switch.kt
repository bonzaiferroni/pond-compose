package pondui.ui.controls

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import pondui.ui.modifiers.magicBackground
import pondui.ui.modifiers.magicDim
import pondui.ui.modifiers.pad
import pondui.ui.modifiers.padEnd
import pondui.ui.modifiers.size
import pondui.ui.theme.Pond
import pondui.utils.electrify

@Composable
fun Switch(
    isOn: Boolean,
    modifier: Modifier = Modifier,
    shape: Shape = Pond.ruler.pill,
    color: Color = Pond.colors.primary,
    content: @Composable () -> Unit = { Box(modifier = Modifier.size(3))},
    onChange: (Boolean) -> Unit
) {
    val animation by animateFloatAsState(if (isOn) 1f else 0f)
    val margin = Pond.ruler.unitSpacing * 2
    Box(
        modifier = modifier.clip(shape)
            .background(Pond.colors.void)
            .actionable { onChange(!isOn) }
            .padding(3.dp)
            .padding(end = margin)
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer {
                    translationX = (margin * animation).toPx()
                }
                .clip(shape)
                .magicBackground(if (isOn) color.electrify(1.3f) else color)
                .pad(1)
        ) {
            content()
        }
    }
}

@Composable
fun Switch(
    isOn: Boolean,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    shape: Shape = Pond.ruler.pill,
    color: Color = Pond.colors.primary,
    onChange: (Boolean) -> Unit
) {
    Switch(
        isOn = isOn,
        modifier = modifier,
        shape = shape,
        color = color,
        content = { Icon(icon, modifier = Modifier.size(3).magicDim(!isOn)) },
        onChange = onChange,
    )
}

@Composable
fun Switch(
    isOn: Boolean,
    text: String,
    modifier: Modifier = Modifier,
    shape: Shape = Pond.ruler.pill,
    color: Color = Pond.colors.primary,
    onChange: (Boolean) -> Unit
) {
    Switch(
        isOn = isOn,
        modifier = modifier,
        shape = shape,
        color = color,
        content = { Text(text, modifier = Modifier.magicDim(!isOn)) },
        onChange = onChange,
    )
}