package pondui.ui.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

abstract class ModularModel: ViewModel() {

    protected fun <T> module(initialState: T, init: SubModel<T>.() -> Unit) = object : SubModel<T>(initialState) {
        override val coroutineScope get() = viewModelScope

        init {
            init()
        }
    }
}