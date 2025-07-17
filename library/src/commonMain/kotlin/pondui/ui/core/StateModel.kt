package pondui.ui.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class StateModel<State>(initialState: State) : ViewModel() {
    protected val _state = MutableStateFlow(initialState)
    val state = _state.asStateFlow()
    val stateNow get() = state.value

    private val jobs = mutableListOf<Job>()

    protected fun setState(block: (State) -> State) {
        _state.value = block(state.value)
    }

    protected fun cancelJobs() {
        this@StateModel.viewModelScope.coroutineContext[Job]?.cancelChildren()
    }

    protected fun <T> Flow<T>.launchCollect(block: (T) -> Unit) = viewModelScope.launch {
        this@launchCollect.collect(block)
    }.also { jobs.add(it) }

    protected fun clearJobs() {
        jobs.forEach { it.cancel() }
        jobs.clear()
    }

    protected fun ioLaunch(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(Dispatchers.Default, block = block)
}