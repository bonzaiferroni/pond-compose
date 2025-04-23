package pondui.ui.core

import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import pondui.ui.nav.NavRoute
import pondui.ui.nav.Navigator
import pondui.ui.theme.ProvideSkyColors

@Composable
fun PondApp(
    initialRoute: NavRoute,
    config: PondConfig,
    navController: NavHostController,
    changeRoute: (NavRoute) -> Unit,
    exitApp: (() -> Unit)?,
) {
    ProvideSkyColors {
        Navigator(
            startRoute = initialRoute,
            config = config,
            navController = navController,
            changeRoute = changeRoute,
            exitApp = exitApp
        )
    }
}