package pondui.ui.core

import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalForInheritanceCoroutinesApi::class)
class ModelState<State>(
    initialState: State,
): StateFlow<State> {
    protected val _state = MutableStateFlow(initialState)

    fun setValue(block: (State) -> State) {
        _state.value = block(_state.value)
    }

    override val value get() = _state.value
    override val replayCache get() = _state.replayCache

    override suspend fun collect(collector: FlowCollector<State>) = _state.collect(collector)
}