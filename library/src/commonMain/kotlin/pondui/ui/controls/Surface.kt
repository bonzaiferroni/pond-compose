package pondui.ui.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pondui.ui.theme.Pond
import pondui.ui.theme.ProvideBookColors

@Composable
fun Surface(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    ProvideBookColors {
        Box(modifier = modifier.background(Pond.localColors.surface)) {
            content()
        }
    }
}