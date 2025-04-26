package pondui.ui.core

import androidx.compose.runtime.*

class AddressContext(
    initialAddress: String?,
): StateModel<AddressState>(AddressState(address = initialAddress)) {

    fun setAddress(path: String) {
        setState { it.copy(address = path) }
    }
}

data class AddressState(
    val address: String? = null,
)

@Composable
fun ProvideAddressContext(
    initialAddress: String? = null,
    updatedAddress: String? = null,
    viewModel: AddressContext = remember { AddressContext(initialAddress) },
    content: @Composable () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(updatedAddress) {
        if (updatedAddress != null) {
            viewModel.setAddress(updatedAddress)
        }
    }

    CompositionLocalProvider(LocalAddressContext provides viewModel) {
        content()
    }
}

val LocalAddressContext = staticCompositionLocalOf<AddressContext?> {
    null
}