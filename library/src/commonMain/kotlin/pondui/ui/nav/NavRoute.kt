package pondui.ui.nav

import androidx.compose.runtime.Immutable

@Immutable
interface NavRoute {
    val title: String
    fun toPath(): String?
}