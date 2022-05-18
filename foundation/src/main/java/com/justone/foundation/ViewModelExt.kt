package com.justone.foundation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState

@Composable
fun <S, A, E> BaseViewModel<S, A, E>.collectAsState(): State<S> {
    return flow.collectAsState()
}
