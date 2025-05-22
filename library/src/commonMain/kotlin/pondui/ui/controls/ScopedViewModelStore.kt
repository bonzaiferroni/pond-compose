package pondui.ui.controls

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner

@Composable
fun ScopedViewModelStore(
    content: @Composable () -> Unit
) {
    // Create and remember a standalone ViewModelStore
    val store = remember { ViewModelStore() }
    // Clear the store when this composable leaves
    DisposableEffect(Unit) {
        onDispose { store.clear() }
    }
    // Provide the custom owner
    val owner = remember {
        object : ViewModelStoreOwner {
            override val viewModelStore: ViewModelStore = store
        }
    }
    CompositionLocalProvider(
        LocalViewModelStoreOwner provides owner
    ) {
        content()
    }
}