package pondui.ui.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
    label: String? = null,
    color: Color = Pond.colors.void,
    shape: Shape = Pond.ruler.defaultCorners,
    padding: PaddingValues = Pond.ruler.unitPadding,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(shape)
            .background(color.copy(.5f))
            .padding(padding)
    ) {
        label?.let {
            Label(
                text = label,
                style = Pond.typo.label.copy(textAlign = TextAlign.Center),
                modifier = Modifier.fillMaxWidth()
                    .background(color.copy(.08f))
                    .padding(Pond.ruler.unitPadding)
            )
        }
        content()
    }
}