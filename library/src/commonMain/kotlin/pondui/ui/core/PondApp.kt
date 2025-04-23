package pondui.ui.core

import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.StateFlow
import pondui.ui.nav.NavRoute
import pondui.ui.nav.Navigator
import pondui.ui.theme.ProvideSkyColors

@Composable
fun PondApp(
    routeState: StateFlow<NavRoute>,
    config: PondConfig,
    changeRoute: (NavRoute) -> Unit,
    exitApp: (() -> Unit)?,
) {
    ProvideSkyColors {
        Navigator(
            routeState = routeState,
            config = config,
            changeRoute = changeRoute,
            exitApp = exitApp
        )
    }
}