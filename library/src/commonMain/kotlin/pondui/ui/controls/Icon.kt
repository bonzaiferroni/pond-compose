package pondui.ui.controls

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toolingGraphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import pondui.ui.theme.Pond

@Composable
fun Icon(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    color: Color = Pond.localColors.content,
    contentScale: ContentScale = ContentScale.Fit
) {
    val colorFilter = remember(color) { if (color == Color.Unspecified) null else ColorFilter.tint(color) }
    Box(
        modifier
            .toolingGraphicsLayer()
            .paint(
                painter = rememberVectorPainter(imageVector),
                colorFilter = colorFilter,
                contentScale = contentScale
            )
    )
}