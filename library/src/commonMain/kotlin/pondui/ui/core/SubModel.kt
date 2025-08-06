package pondui.ui.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class SubModel<State>() {

    protected abstract val state: ModelState<State>

    protected abstract val viewModel: ViewModel
    protected val viewModelScope get() = viewModel.viewModelScope

    protected val stateNow get() = state.value

    val stateFlow: StateFlow<State> get() = state

    private val jobs = mutableListOf<Job>()

    protected fun setState(block: (State) -> State) = state.setValue(block)

    protected fun cancelViewModelScopeJobs() {
        viewModelScope.coroutineContext[Job]?.cancelChildren()
    }

    protected fun <T> Flow<T>.launchCollect(block: (T) -> Unit) = viewModelScope.launch {
        this@launchCollect.collect(block)
    }.also { jobs.add(it) }

    fun clearJobs() {
        jobs.forEach { it.cancel() }
        jobs.clear()
    }

    protected fun ioLaunch(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(Dispatchers.Default, block = block)
}