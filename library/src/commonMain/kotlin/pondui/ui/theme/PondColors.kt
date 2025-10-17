package pondui.ui.theme

import androidx.compose.ui.graphics.Color
import pondui.utils.mixWith

interface PondColors {
    val accent: Color
    val primary: Color
    val secondary: Color
    val selection: Color
    val negation: Color
    val data: Color
    val timer: Color
    val disabled: Color
    val creationContent: Color
    val actionContent: Color
    val selectionContent: Color
    val deletionContent: Color
    val contentSky: Color
    val surfaceSky: Color
    val contentBook: Color
    val surfaceBook: Color
    val background: Color
    val artBackground: Color
    val void: Color
    val selectionVoid: Color
    val creationVoid: Color
    val deletionVoid: Color
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
    val section: Color,
    val dangerContent: Color,
    val selectedContent: Color,
) {
    val contentDim get() = content.copy(.75f)
}

object DefaultColors : PondColors{
    override val accent = Color(0xffd12bb5) // 0xff458f58 // 0xff559f6c // 0xff4c8e60
    override val primary = Color(0xff017a8a) // 0xFF009489 // 0xff018a80
    override val secondary = Color(0xff5e5045)
    override val negation = Color(0xff884444)
    override val selection = Color(0xff775f93)
    override val disabled = Color(0xff6e7e6f)
    override val data = Color(0xffa3765a)
    override val timer = Color(0xff78675b)
    override val contentSky = Color(0xFFf5f6f6)
    override val creationContent = accent.mixWith(contentSky)
    override val actionContent = primary.mixWith(contentSky)
    override val selectionContent = selection.mixWith(contentSky)
    override val deletionContent = negation.mixWith(contentSky)
    override val surfaceSky = Color.Transparent
    override val contentBook = Color(0xFF1d190e)
    override val surfaceBook = Color(0xFFcecfcf)
    override val background = Color(0xff090d0d) // 0xff18635a // 0xFF1b7161 // 0xff171616 // 0xFF1b7161 // 0xff1e554f
    override val artBackground = Color(0xff18635a)
    override val void = Color(0xff3b4242)
    override val selectionVoid = void.mixWith(selection, .25f)
    override val creationVoid = void.mixWith(accent, .25f)
    override val deletionVoid = void.mixWith(negation, .25f)
    override val glow = Color(0xffecffb6)
    override val swatches = listOf(
        Color(0xff6fffe2),
        Color(0xfffebcb8),
        Color(0xff86e864),
        Color(0xfff6c954),
        Color(0xffba936e),
        Color(0xffe39f60),
        Color(0xffc1a1e1),
        Color(0xff96b9df),
        Color(0xffa5a5a5),
    )
}

private val darkPurpleBg = Color(0xff1d003c)