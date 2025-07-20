package pondui.ui.theme

import androidx.compose.ui.graphics.Color

interface PondColors {
    val primary: Color
    val secondary: Color
    val tertiary: Color
    val selected: Color
    val data: Color
    val disabled: Color
    val danger: Color
    val contentSky: Color
    val surfaceSky: Color
    val contentBook: Color
    val surfaceBook: Color
    val background: Color
    val void: Color
    val glow: Color
    val swatches: List<Color>

    fun getSwatchFromIndex(index: Int) = swatches[index % swatches.size]
    fun getSwatchFromIndex(index: Long) = getSwatchFromIndex(index.toInt())
}

enum class ColorMode {
    Sky,
    Book,
}

data class PondLocalColors(
    val mode: ColorMode,
    val content: Color,
    val surface: Color,
    val highlight: Color,
    val sectionSurface: Color,
) {
    val contentDim get() = content.copy(.75f)
}

object DefaultColors : PondColors{
    override val primary = Color(0xff4c8e60) // 0xff559f6c
    override val secondary = Color(0xFF009489)
    override val tertiary = Color(0xff5e5045)
    override val disabled = Color(0xff6e7e6f)
    override val selected = Color(0xff775f93)
    override val data = Color(0xffa3765a)
    override val danger = Color(0xff884444)
    override val contentSky = Color(0xFFf5f6f6)
    override val surfaceSky = Color.Transparent
    override val contentBook = Color(0xFF1d190e)
    override val surfaceBook = Color(0xFFcecfcf)
    override val background = Color(0xFF1b7161) // 0xFF1b7161 // 0xff171616
    override val void = Color(0xff3b4242)
    override val glow = Color(0xffecffb6)
    override val swatches = listOf(
        Color(0xff6fffe2),
        Color(0xfffebcb8),
        Color(0xFFA11B0A),
        Color(0xFFE3A100),
        Color(0xFF6B3E26),
        Color(0xFFDC6A00),
        Color(0xFF7D3CCF),
        Color(0xFF00B8C4),
        Color(0xFF737373),
    )
}