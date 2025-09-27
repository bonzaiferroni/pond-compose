package pondui.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import org.jetbrains.compose.resources.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
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
    val italic: TextStyle
    val bold: TextStyle
    val light: TextStyle

    val baseFontSize: TextUnit
    val smallFontSize: TextUnit
}

class DefaultTypography(
    scale: Float,
    baseFontSize: Float,
    baseFont: FontFamily,
    boldFont: FontFamily,
    italicFont: FontFamily,
    lightFont: FontFamily,
    h1Font: FontFamily,
    h2Font: FontFamily,
    h4Font: FontFamily,
): PondTypography {

    val base = TextStyle.Default.copy(
        fontSize = baseFontSize.sp * scale,
        fontFamily = baseFont
    )

    override val baseFontSize = baseFontSize.sp
    override val smallFontSize = (baseFontSize - 2).sp

    override val bold = base.copy(
        fontFamily = boldFont,
        fontWeight = FontWeight.Bold
    )
    override val italic = base.copy(
        fontFamily = italicFont,
        fontStyle = FontStyle.Italic,
    )
    override val light = base.copy(
        fontFamily = lightFont,
        fontWeight = FontWeight.Light
    )

    override val title = base.copy(
        fontSize = (baseFontSize + 6).sp * scale, // 20sp
        fontFamily = h2Font
    )
    override val h1 = base.copy(
        fontSize = (baseFontSize + 14).sp * scale, // 28sp
        fontWeight = FontWeight.Light,
        fontFamily = h1Font,
    )
    override val h2 = base.copy(
        fontSize = (baseFontSize + 10).sp * scale, // 24sp
        fontWeight = FontWeight.Light,
        fontFamily = h2Font,
    )
    override val h3 = base.copy(
        fontSize = (baseFontSize + 6).sp * scale, // 20sp
        fontWeight = FontWeight.Light,
        fontFamily = h2Font,
    )
    override val h4 = base.copy(
        fontSize = (baseFontSize + 2).sp * scale, // 16sp
        fontWeight = FontWeight.Normal,
        fontFamily = h4Font,
    )
    override val h5 = base.copy(
        fontSize = baseFontSize.sp * scale,
        fontWeight = FontWeight.Bold,
        fontFamily = h4Font,
    )

    override val body = base
    override val bodyLarge = base.copy(
        fontSize = (baseFontSize + 2).sp * scale // 16sp
    )
    override val label = base.copy(
        fontSize = (baseFontSize - 2).sp * scale // 12sp
    )
    override val small = base.copy(
        fontSize = (baseFontSize - 2).sp * scale // 12sp
    )
}

@Composable
fun useFamily(resource: FontResource, weight: FontWeight = FontWeight.Normal) = FontFamily(
    Font(resource, weight = weight)
)