package com.example.justone.model

import com.example.foundation.network.ResourceState

data class JustOneState(
    val words: List<String> = emptyList(),
    val translation: ResourceState<String> = ResourceState.Loading,
    val wordsNumber: Int,
    val timer: Int,
    val isLoading: Boolean = false
)
