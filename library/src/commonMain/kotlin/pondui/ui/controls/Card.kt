package pondui.ui.controls

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import pondui.ui.theme.Pond
import pondui.ui.theme.ProvideBookColors

@Composable
fun Card(
    shape: Shape = Pond.ruler.rounded,
    background: Color = Pond.colors.surfaceBook,
    innerPadding: Dp = Pond.ruler.innerSpacing,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    ProvideBookColors {
        Column(
            modifier = modifier.clip(shape)
                .background(background)
                .padding(innerPadding)
                .animateContentSize(),
            verticalArrangement = Pond.ruler.columnUnit
        ) {
            content()
        }
    }
}