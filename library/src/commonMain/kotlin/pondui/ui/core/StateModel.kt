package pondui.ui.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class StateModel<State>(initialState: State) : ViewModel() {
    protected val _state = MutableStateFlow(initialState)
    val state = _state.asStateFlow()
    val stateNow get() = state.value

    protected fun setState(block: (State) -> State) {
        _state.value = block(state.value)
    }

    protected fun cancelJobs() {
        viewModelScope.coroutineContext[Job]?.cancelChildren()
    }
}