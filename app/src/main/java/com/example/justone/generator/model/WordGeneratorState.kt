package com.example.justone.generator.model

data class WordGeneratorState(
    val words: List<String>,
    val wordsNumber: Int,
    val timer: Int,
    val isLoading: Boolean = false
)
