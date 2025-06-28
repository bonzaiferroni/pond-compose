package pondui.ui.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class SubModel<State>(
    initialState: State,
) {
    protected abstract val coroutineScope: CoroutineScope

    protected val _state = MutableStateFlow(initialState)
    val state = _state.asStateFlow()
    protected val stateNow get() = state.value

    private val jobs = mutableListOf<Job>()

    protected fun setState(block: (State) -> State) {
        _state.value = block(state.value)
    }

    protected fun cancelJobs() {
        coroutineScope.coroutineContext[Job]?.cancelChildren()
    }

    protected fun <T> Flow<T>.launchCollect(block: (T) -> Unit) = coroutineScope.launch {
        this@launchCollect.collect(block)
    }.also { jobs.add(it) }

    protected fun clearJobs() {
        jobs.forEach { it.cancel() }
        jobs.clear()
    }
}