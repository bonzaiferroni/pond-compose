package pondui.ui.nav

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import pondui.ui.controls.Column
import pondui.ui.core.LocalAddressContext
import pondui.ui.core.PondConfig
import pondui.ui.theme.Pond

@Composable
fun Navigator(
    changeRoute: (NavRoute) -> Unit,
    config: PondConfig,
    exitApp: (() -> Unit)?,
    navController: NavHostController = rememberNavController(),
    nav: NavigatorModel = viewModel { NavigatorModel(config.home, navController) }
) {
    val state by nav.stateFlow.collectAsState()
    val address = LocalAddressContext.current?.stateFlow?.collectAsState()?.value?.address

    if (address != null) {
        LaunchedEffect(address) {
            config.toRoute(address)?.let {
                nav.go(it)
            }
        }
    }

    LaunchedEffect(state.route) {
        changeRoute(state.route)
    }

    CompositionLocalProvider(LocalNav provides nav) {
        Portal(
            config = config,
            exitAction = exitApp
        ) {
            NavHost(
                navController = navController,
                startDestination = config.home,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                for (route in config.routes) {
                    route.content(this)
                }
            }
        }
    }
}

inline fun <reified T : NavRoute> NavGraphBuilder.defaultScreen(
    horizontalPadding: Dp? = null,
    crossinline content: @Composable (T) -> Unit
) {
    composable<T> { backStackEntry ->
        val route: T = backStackEntry.toRoute()
        Column(
            gap = 1,
            modifier = Modifier.padding(
                top = 0.dp,
                start = horizontalPadding ?: Pond.ruler.unitSpacing,
                end = horizontalPadding ?: Pond.ruler.unitSpacing,
                bottom = 0.dp,
            )
        ) {
            content(route)
        }
    }
}

val LocalNav = staticCompositionLocalOf<Nav> {
    error("No Nav provided")
}
