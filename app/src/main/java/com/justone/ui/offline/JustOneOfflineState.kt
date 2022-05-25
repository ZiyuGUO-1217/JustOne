package com.justone.ui.offline

import com.justone.foundation.network.ResourceState

data class JustOneOfflineState(
    val words: ResourceState<List<String>> = ResourceState.Empty,
    val translation: ResourceState<String> = ResourceState.Loading,
    val keyword: String = "",
    val wordsNumber: Int,
    val submittedClues: List<String> = emptyList(),
    val clueTimer: Int,
    val guessTimer: Int,
    val playersNumber: Int = 0,
    val isAnswerCorrect: Boolean = false
)
