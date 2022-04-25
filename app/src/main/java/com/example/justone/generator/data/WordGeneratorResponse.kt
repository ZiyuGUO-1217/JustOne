package com.example.justone.generator.data

data class WordGeneratorResponse(
    val status: String,
    val data: List<String>,
    val size: Int
)
