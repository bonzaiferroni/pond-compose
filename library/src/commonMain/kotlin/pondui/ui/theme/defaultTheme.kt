package pondui.ui.theme

import androidx.compose.ui.text.font.FontFamily
import pondui.utils.darken
import pondui.utils.lighten
import pondui.utils.mixWith

fun defaultTheme(
    scale: Float = 1f,
    baseFontSize: Float = 14f,
    baseFont: FontFamily = FontFamily.Default,
    boldFont: FontFamily = baseFont,
    italicFont: FontFamily = baseFont,
    lightFont: FontFamily = baseFont,
    h1Font: FontFamily = lightFont,
    h2Font: FontFamily = lightFont,
    h4Font: FontFamily = baseFont,
    monoFont: FontFamily = baseFont,
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
        baseFontSize = baseFontSize,
        baseFont = baseFont,
        boldFont = boldFont,
        italicFont = italicFont,
        lightFont = lightFont,
        h1Font = h1Font,
        h2Font = h2Font,
        h4Font = h4Font,
        monoFont = monoFont
    )
}