package pondui.ui.nav

import kotlinx.coroutines.flow.StateFlow

interface Nav {
    val state: StateFlow<NavState>

    fun go(route: NavRoute)
    fun goBack()
    fun setRoute(route: NavRoute, addToBackStack: Boolean = false)
}