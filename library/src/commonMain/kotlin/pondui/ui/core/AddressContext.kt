package pondui.ui.core

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.StateFlow
import pondui.ui.nav.NavRoute

class AddressContext(
    private val config: PondConfig,
): StateModel<AddressState>(AddressState(addressRoute = config.home)) {

    fun setAddress(address: String?) {
        if (address == null || address == stateNow.address) return
        val route = config.toRoute(address) ?: stateNow.addressRoute
        setState { it.copy(address = address, addressRoute = route) }
    }
}

data class AddressState(
    val address: String? = null,
    val addressRoute: NavRoute
)

@Composable
fun ProvideAddressContext(
    address: String? = null,
    config: PondConfig,
    viewModel: AddressContext = viewModel { AddressContext(config) },
    content: @Composable () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(address) {
        viewModel.setAddress(address)
    }

    CompositionLocalProvider(LocalAddressContext provides viewModel) {
        content()
    }
}

val LocalAddressContext = staticCompositionLocalOf<AddressContext?> {
    null
}