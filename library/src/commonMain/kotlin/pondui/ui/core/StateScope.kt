package pondui.ui.core

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class StateScope<State>(initialState: State) {
    protected val _state = MutableStateFlow(initialState)
    val state = _state.asStateFlow()
    val stateNow get() = state.value

    protected fun setState(block: (State) -> State) {
        _state.value = block(state.value)
    }
}