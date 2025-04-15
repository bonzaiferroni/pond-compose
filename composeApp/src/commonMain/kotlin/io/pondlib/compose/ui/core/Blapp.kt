package io.pondlib.compose.ui.core

import androidx.compose.runtime.*
import io.pondlib.compose.ui.nav.NavRoute
import io.pondlib.compose.ui.nav.Navigator
import io.pondlib.compose.ui.theme.ProvideSkyColors

@Composable
fun Blapp(
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