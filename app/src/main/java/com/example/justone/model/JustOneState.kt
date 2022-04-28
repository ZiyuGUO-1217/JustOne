package com.example.justone.model

import com.example.foundation.network.ResourceState

data class JustOneState(
    val words: ResourceState<List<String>> = ResourceState.Empty,
    val translation: ResourceState<String> = ResourceState.Loading,
    val wordsNumber: Int,
    val timer: Int
)
