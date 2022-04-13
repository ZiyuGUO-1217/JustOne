package com.example.justone.model

data class WordGeneratorState(
    val words: List<String>,
    val wordsNumber: Int,
    val isLoading: Boolean = false
)
