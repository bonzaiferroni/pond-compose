package pondui.ui.core

import androidx.compose.runtime.*
import pondui.ui.nav.NavRoute
import pondui.ui.nav.Navigator
import pondui.ui.theme.ProvideSkyColors

@Composable
fun PondApp(
    initialRoute: NavRoute,
    changeRoute: (NavRoute) -> Unit,
    config: PondConfig,
    exitApp: (() -> Unit)?,
) {
    ProvideSkyColors {
        Navigator(
            startRoute = initialRoute,
            changeRoute = changeRoute,
            config = config,
            exitApp = exitApp
        )
    }
}