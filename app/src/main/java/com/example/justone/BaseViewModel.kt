package com.example.justone

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
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

    protected var state
        get() = _flow.value
        private set(value) {
            _flow.value = value
        }

    protected fun updateState(block: S.() -> S) {
        state = block(state)
    }
}

@Composable
fun <S, A> BaseViewModel<S, A>.collectAsState(
    context: CoroutineContext = EmptyCoroutineContext
): State<S> {
    return flow.collectAsState(context = context)
}
