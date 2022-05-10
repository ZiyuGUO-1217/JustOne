package com.justone.data.model

import java.io.Serializable

data class WordTranslatorResponse(
    val confidence: Float,
    val target: String,
    val rc: Int
) : Serializable
