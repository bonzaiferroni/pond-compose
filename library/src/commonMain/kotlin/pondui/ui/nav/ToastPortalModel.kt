package pondui.ui.nav

import androidx.lifecycle.ViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import pondui.ui.core.SubModel
import pondui.ui.core.ModelState

class ToastPortalModel(
    override val viewModel: ViewModel
): SubModel<ToastPortalState>() {

    override val state = ModelState(ToastPortalState())

    fun setToast(toast: Toast) {
        addToast(toast)
    }

    private fun addToast(toast: Toast) {
        setState { it.copy(toasts = (it.toasts + toast).takeLast(5)) }
    }
}

data class ToastPortalState(
    val toasts: List<Toast> = emptyList()
)

enum class ToastType {
    Default,
    Error,
}

data class Toast(
    val content: String,
    val type: ToastType,
    val time: Instant = Clock.System.now(),
)

