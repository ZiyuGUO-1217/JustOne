package com.justone.model.online

import com.justone.foundation.network.ResourceState

data class JustOneOnlineState(
    val words: ResourceState<List<String>> = ResourceState.Empty,
    val translation: ResourceState<String> = ResourceState.Loading,
    val keyword: String = "",
    val collectedClues: List<String> = emptyList(),
    val timer: Int,
    val isOnlineSetupFinished: Boolean = false,
    val playerList: List<String> = emptyList()
)
