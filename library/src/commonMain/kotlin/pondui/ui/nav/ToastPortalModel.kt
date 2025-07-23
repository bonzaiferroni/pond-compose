package pondui.ui.nav

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import pondui.ui.core.SubModel
import pondui.ui.core.ViewState
import kotlin.time.Duration.Companion.seconds

class ToastPortalModel(
    override val viewModel: ViewModel
): SubModel<ToastPortalState>() {

    override val state = ViewState(ToastPortalState())

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

