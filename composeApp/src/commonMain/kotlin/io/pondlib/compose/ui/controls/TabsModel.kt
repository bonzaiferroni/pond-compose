package io.pondlib.compose.ui.controls

import io.pondlib.compose.ui.core.StateModel

class TabsModel(initialTab: String?): StateModel<TabsState>(TabsState(initialTab)) {
    fun setTab(tab: String?) {
        setState { it.copy(tab = tab) }
    }
}

data class TabsState(
    val tab: String? = null,
)