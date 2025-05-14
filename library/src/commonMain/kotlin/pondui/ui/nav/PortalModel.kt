package pondui.ui.nav

import pondui.ui.core.StateModel

class PortalModel: StateModel<PortalState>(PortalState()) {

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