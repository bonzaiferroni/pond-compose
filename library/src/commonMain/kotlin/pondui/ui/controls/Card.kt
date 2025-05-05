package pondui.ui.controls

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import pondui.ui.theme.Pond
import pondui.ui.theme.ProvideBookColors

@Composable
fun Card(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    ProvideBookColors {
        Column(
            modifier = modifier.background(Pond.localColors.surface)
                .padding(Pond.ruler.innerPadding)
                .animateContentSize(),
            verticalArrangement = Pond.ruler.columnTight
        ) {
            content()
        }
    }
}