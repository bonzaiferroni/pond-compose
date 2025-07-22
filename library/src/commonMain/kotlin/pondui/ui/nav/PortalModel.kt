package pondui.ui.nav

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import pondui.ui.core.StateModel
import pondui.utils.Broadcaster

class PortalModel : StateModel<PortalState>(PortalState()) {

    val cloudPortal = CloudPortalModel(this)

    fun setHoverText(text: String) {
        setState { it.copy(hoverText = text) }
    }

    fun setBottomBarIsVisible(isVisible: Boolean) {
        setState { it.copy(bottomBarIsVisible = isVisible) }
    }

    fun setTitle(currentTitle: String?) {
        setState { it.copy(currentTitle = currentTitle) }
    }
}

data class PortalState(
    val hoverText: String = "",
    val currentTitle: String? = null,
    val topBarIsVisible: Boolean = true,
    val bottomBarIsVisible: Boolean = true,
)