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

    fun setHoverText(text: String) {
        setState { it.copy(hoverText = text) }
    }

    fun setBottomBarIsVisible(isVisible: Boolean) {
        setState { it.copy(bottomBarIsVisible = isVisible) }
    }

    fun setTitle(currentTitle: String?) {
        setState { it.copy(currentTitle = currentTitle) }
    }

    fun setDialogContent(
        title: String,
        isVisible: Boolean,
        dismissDialog: () -> Unit,
        content: @Composable () -> Unit
    ) {
        if (!isVisible && stateNow.dialogTitle != title) return
        setState { it.copy(
            dialogTitle = title,
            dialogContent = content,
            isDialogVisible = isVisible,
            dismissDialog = dismissDialog
        ) }
    }

    fun hideDialog() {
        setState { it.copy(isDialogVisible = false) }
    }
}

data class PortalState(
    val hoverText: String = "",
    val currentTitle: String? = null,
    val topBarIsVisible: Boolean = true,
    val bottomBarIsVisible: Boolean = true,
    val dialogTitle: String = "",
    val isDialogVisible: Boolean = false,
    val dismissDialog: () -> Unit = { },
    val dialogContent: @Composable () -> Unit = { }
)