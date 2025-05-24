package pondui.ui.core

import androidx.compose.runtime.*
import pondui.ui.nav.NavRoute
import pondui.ui.nav.Navigator

@Composable
fun PondApp(
    config: PondConfig,
    changeRoute: (NavRoute) -> Unit,
    exitApp: (() -> Unit)?,
) {
    Navigator(
        config = config,
        changeRoute = changeRoute,
        exitApp = exitApp
    )
}