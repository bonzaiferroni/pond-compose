package io.pondlib.compose.theme.controls

import io.pondlib.compose.theme.core.StateModel

class TabsModel(initialTab: String?): StateModel<TabsState>(TabsState(initialTab)) {
    fun setTab(tab: String?) {
        setState { it.copy(tab = tab) }
    }
}

data class TabsState(
    val tab: String? = null,
)