package pondui.ui.theme

import androidx.compose.ui.text.font.FontFamily
import pondui.utils.darken
import pondui.utils.lighten
import pondui.utils.mixWith

fun defaultTheme(
    scale: Float = 1f,
    baseFont: FontFamily = FontFamily.Default,
    h1Font: FontFamily = FontFamily.Default,
    h2Font: FontFamily = FontFamily.Default,
    h4Font: FontFamily = FontFamily.Default
) = object : PondTheme {
    override val layout: PondRuler = DefaultRuler(scale)

    override val colors: PondColors = DefaultColors

    override val skyColors = PondLocalColors(
        mode = ColorMode.Sky,
        content = colors.contentSky,
        surface = colors.surfaceSky,
        highlight = colors.glow.lighten(.4f),
        sectionSurface = colors.void.copy(.3f),
        // sectionSurface = colors.background.mixWith(colors.void),
        dangerContent = colors.negation.lighten(.4f),
        selectedContent = colors.selection.lighten(.4f)
    )

    override val bookColors = PondLocalColors(
        mode = ColorMode.Book,
        content = colors.contentBook,
        surface = colors.surfaceBook,
        highlight = colors.glow.darken(.4f),
        sectionSurface = colors.background.mixWith(colors.surfaceBook, .8f),
        dangerContent = colors.negation.darken(.4f),
        selectedContent = colors.selection.darken(.4f)
    )

    override val typography = DefaultTypography(
        scale = scale,
        baseFont = baseFont,
        h1Font = h1Font,
        h2Font = h2Font,
        h4Font = h4Font
    )
}