package pondui.ui.nav

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import pondui.ui.core.SubModel
import pondui.ui.core.ViewState

class CloudPortalModel(
    override val viewModel: ViewModel,
): SubModel<CloudPortalState>() {

    override val state = ViewState(CloudPortalState())

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

data class CloudPortalState(
    val dialogTitle: String = "",
    val isDialogVisible: Boolean = false,
    val dismissDialog: () -> Unit = { },
    val dialogContent: @Composable () -> Unit = { },
)
