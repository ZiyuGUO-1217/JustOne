package com.justone.data.model

data class WordGeneratorResponse(
    val status: String,
    val data: List<String>,
    val size: Int
)
