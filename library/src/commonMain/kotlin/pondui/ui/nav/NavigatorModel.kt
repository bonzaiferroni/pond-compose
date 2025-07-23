package pondui.ui.nav

import androidx.navigation.NavController
import pondui.ui.core.StateModel
import pondui.ui.core.ViewState

class NavigatorModel(
    initialRoute: NavRoute,
    private val navController: NavController,
) : StateModel<NavState>(), Nav {
    override val state = ViewState(NavState(initialRoute))

    private val backStack: MutableList<NavRoute> = mutableListOf()

    override fun go(route: NavRoute) {
        addCurrentToBackStack()
        navController.navigate(route)
        setState { state ->
            state.copy(
                route = route,
                backRoute = backStack.lastOrNull()
            )
        }
    }

    override fun goBack() {
        if (backStack.isEmpty()) return
        val lastRoute = backStack.removeLast()
        navController.navigateUp()
        // navController.navigate(lastRoute)
        setRoute(lastRoute)
    }

    override fun setRoute(route: NavRoute, addToBackStack: Boolean) {
        // if (addToBackStack) addCurrentToBackStack()
        setState { state ->
            state.copy(
                route = route,
                backRoute = backStack.lastOrNull()
            )
        }
    }

    private fun addCurrentToBackStack() {
        backStack.add(stateNow.route)
        if (backStack.size > 40) backStack.removeAt(0)
    }
}

data class NavState(
    val route: NavRoute,
    val backRoute: NavRoute? = null,
)

const val NAV_KEY = "nav_route"
