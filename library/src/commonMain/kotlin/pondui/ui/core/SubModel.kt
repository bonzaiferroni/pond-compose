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
import kotlinx.coroutines.withContext

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

    fun clearJobs() {
        jobs.forEach { it.cancel() }
        jobs.clear()
    }

    protected fun ioLaunch(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(Dispatchers.IO, block = block)

    protected fun <T> ioCollect(flow: StateFlow<T>, block: suspend (T) -> Unit) = ioLaunch { flow.collect { block(it) } }

    suspend fun withMain(block: CoroutineScope.() -> Unit) =
        withContext(Dispatchers.Main, block = block)

    protected suspend fun setStateWithMain(block: (State) -> State) {
        withMain {
            setState(block)
        }
    }
}