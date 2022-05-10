package com.justone.model.offline

import com.justone.foundation.network.ResourceState

data class JustOneOfflineState(
    val words: ResourceState<List<String>> = ResourceState.Empty,
    val translation: ResourceState<String> = ResourceState.Loading,
    val keyword: String = "",
    val submittedClues: List<String> = emptyList(),
    val timer: Int
)
