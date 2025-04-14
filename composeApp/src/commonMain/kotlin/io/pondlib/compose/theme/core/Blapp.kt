package io.pondlib.compose.theme.core

import androidx.compose.runtime.*
import io.pondlib.compose.theme.nav.NavRoute
import io.pondlib.compose.theme.nav.Navigator
import io.pondlib.compose.theme.theme.ProvideSkyColors

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