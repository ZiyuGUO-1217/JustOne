package com.example.foundation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<S, A> : ViewModel() {
    protected abstract fun configureInitState(): S
    abstract fun dispatch(action: A)

    private val _flow by lazy {
        MutableStateFlow(configureInitState())
    }

    val flow by lazy {
        _flow.asStateFlow()
    }

    var state
        get() = _flow.value
        private set(value) {
            _flow.value = value
        }

    protected fun updateState(block: S.() -> S) {
        state = block(state)
    }
}
