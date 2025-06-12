package pondui.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle

fun TextStyle.addShadow(
    offset: Float = 2f,
    blurRadius: Float = 8f,
    color: Color = Color.Black
) = this.copy(
    shadow = Shadow(
        color = color,
        offset = Offset(offset, offset),
        blurRadius = blurRadius
    )
)