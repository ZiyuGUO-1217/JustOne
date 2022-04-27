package com.example.justone.data

import java.io.Serializable

data class WordTranslatorResponse(
    val confidence: Float,
    val target: String,
    val rc: Int
) : Serializable
