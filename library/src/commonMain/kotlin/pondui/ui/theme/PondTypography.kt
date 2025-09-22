package pondui.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import org.jetbrains.compose.resources.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.FontResource

interface PondTypography {
    val title: TextStyle
    val h1: TextStyle
    val h2: TextStyle
    val h3: TextStyle
    val h4: TextStyle
    val h5: TextStyle
    val body: TextStyle
    val bodyLarge: TextStyle
    val label: TextStyle
    val small: TextStyle
}

class DefaultTypography(
    scale: Float,
    baseFont: FontFamily,
    h1Font: FontFamily,
    h2Font: FontFamily,
    h4Font: FontFamily,
): PondTypography {

    val base = TextStyle.Default.copy(
        fontSize = 14.sp * scale,
        fontFamily = baseFont
    )

    override val title = base.copy(
        fontSize = 20.sp * scale,
        fontFamily = h2Font
    )
    override val h1 = base.copy(
        fontSize = 28.sp * scale,
        fontWeight = FontWeight.Light,
        fontFamily = h1Font,
    )
    override val h2 = base.copy(
        fontSize = 24.sp * scale,
        fontWeight = FontWeight.Light,
        fontFamily = h2Font,
    )
    override val h3 = base.copy(
        fontSize = 20.sp * scale,
        fontWeight = FontWeight.Light,
        fontFamily = h2Font,
    )
    override val h4 = base.copy(
        fontSize = 16.sp * scale,
        fontWeight = FontWeight.Normal,
        fontFamily = h4Font,
    )
    override val h5 = base.copy(
        fontSize = 14.sp * scale,
        fontWeight = FontWeight.Bold,
        fontFamily = h4Font,
    )

    override val body = base
    override val bodyLarge = base.copy(
        fontSize = 16.sp * scale
    )
    override val label = base.copy(
        fontSize = 12.sp * scale
    )
    override val small = base.copy(
        fontSize = 12.sp * scale
    )
}

@Composable
fun useFamily(resource: FontResource, weight: FontWeight = FontWeight.Normal) = FontFamily(
    Font(resource, weight = weight)
)