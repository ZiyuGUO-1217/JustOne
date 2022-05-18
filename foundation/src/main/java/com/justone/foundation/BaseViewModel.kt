package com.justone.foundation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<S, A, E> : ViewModel() {
    protected abstract fun configureInitState(): S
    abstract fun dispatch(action: A)

    private val _event = MutableSharedFlow<E>()
    val event = _event.asSharedFlow()

    private val _flow by lazy {
        MutableStateFlow(configureInitState())
    }

    val flow by lazy {
        _flow.asStateFlow()
    }

    protected var state
        get() = _flow.value
        private set(value) {
            _flow.value = value
        }

    protected fun updateState(block: S.() -> S) {
        state = block(state)
    }

    protected fun sendEvent(event: E) {
        viewModelScope.launch {
            _event.emit(event)
        }
    }
}
