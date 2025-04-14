package newsref.app.pond.nav

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import newsref.app.pond.behavior.SlideIn
import newsref.app.pond.theme.*
import newsref.app.pond.core.PondConfig

@Composable
fun Navigator(
    startRoute: NavRoute,
    changeRoute: (NavRoute) -> Unit,
    config: PondConfig,
    exitApp: (() -> Unit)?,
    navController: NavHostController = rememberNavController(),
    nav: NavigatorModel = viewModel { NavigatorModel(startRoute, navController) }
) {
    val state by nav.state.collectAsState()

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
                startDestination = startRoute,
                modifier = Modifier
                    // .padding(innerPadding)
                    .fillMaxSize()
                // .background(MaterialTheme.colorScheme.surface)
                // .verticalScroll(rememberScrollState())
            ) {
                config.navGraph(this)
            }
        }
    }
}

inline fun <reified T: NavRoute> NavGraphBuilder.defaultScreen(
    crossinline content: @Composable (T) -> Unit
) {
    composable<T> { backStackEntry ->
        val route: T = backStackEntry.toRoute()
        content(route)
    }
}

@Composable
fun DefaultSurface(
    content: @Composable() () -> Unit
) {
    SlideIn {
        Column(verticalArrangement = Pond.ruler.columnSpaced) {
            content()
        }
    }
}

val LocalNav = staticCompositionLocalOf<Nav> {
    error("No Nav provided")
}
