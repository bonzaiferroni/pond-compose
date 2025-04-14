package io.pondlib.compose.theme.theme

import androidx.compose.runtime.Composable
import io.pondlib.compose.utils.darken
import newsref.app.pond.theme.DefaultTypography

@Composable
fun DefaultTheme() = object : PondTheme {
    override val layout: PondRuler = DefaultRuler

    override val colors: PondColors = DefaultColors

    override val skyColors = PondLocalColors(
        mode = ColorMode.Sky,
        content = colors.contentSky,
        surface = colors.surfaceSky,
        highlight = colors.shine.darken(-.4f)
    )

    override val bookColors = PondLocalColors(
        mode = ColorMode.Book,
        content = colors.contentBook,
        surface = colors.surfaceBook,
        highlight = colors.shine.darken(.4f)
    )

    override val typography = DefaultTypography()
}