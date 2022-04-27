package com.example.justone.data

data class WordGeneratorResponse(
    val status: String,
    val data: List<String>,
    val size: Int
)
