package pondui.ui.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pondui.ui.theme.Pond

@Composable
fun Divider(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(1.dp)
            .background(Pond.localColors.content)
    )
}