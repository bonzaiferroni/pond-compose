package pondui.ui.controls

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pondui.utils.modifyIfTrue
import pondui.ui.theme.Pond

@Composable
fun Checkbox(
    value: Boolean,
    onValueChanged: (Boolean) -> Unit
) {
    val animate by animateFloatAsState(if (value) 1f else 0f)

    Box(
        modifier = Modifier.size(CheckboxSize)
            .clip(Pond.ruler.smallRounded)
            .clickable { onValueChanged(!value) }
            .background(Pond.colors.primary)
            .padding(Pond.ruler.innerPadding)
    ) {
        Text(
            text = "🤙", style = TextStyle(textAlign = TextAlign.Center),
            modifier = Modifier.graphicsLayer {
                scaleX = animate
                scaleY = animate
                rotationZ = 360 * animate
                transformOrigin = TransformOrigin.Center
            }
        )
    }
}

@Composable
fun LabelCheckbox(
    value: Boolean,
    onValueChanged: (Boolean) -> Unit,
    label: String,
) {
    Row(
        horizontalArrangement = Pond.ruler.rowTight,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(value, onValueChanged)
        Text(label)
    }
}

private val CheckboxSize = 26.dp
