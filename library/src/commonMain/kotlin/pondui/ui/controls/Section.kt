package pondui.ui.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import pondui.ui.theme.Pond

@Composable
fun Section(
    modifier: Modifier = Modifier,
    label: String? = null,
    background: Color = Pond.localColors.sectionSurface,
    shape: Shape = Pond.ruler.unitCorners,
    padding: PaddingValues = Pond.ruler.unitPadding,
    content: @Composable () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(shape)
            .background(background)
            .padding(padding)
    ) {
        label?.let {
            Label(
                text = label,
                style = Pond.typo.label.copy(textAlign = TextAlign.Center),
                modifier = Modifier.fillMaxWidth()
                    .background(background.copy(.08f))
                    .padding(Pond.ruler.unitPadding)
            )
        }
        content()
    }
}