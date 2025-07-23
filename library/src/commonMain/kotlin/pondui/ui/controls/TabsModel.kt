package pondui.ui.controls

import pondui.ui.core.StateModel
import pondui.ui.core.ViewState

class TabsModel(initialTab: String?): StateModel<TabsState>() {
    override val state = ViewState(TabsState(initialTab))
    fun setTab(tab: String?) {
        setState { it.copy(tab = tab) }
    }
}

data class TabsState(
    val tab: String? = null,
)
