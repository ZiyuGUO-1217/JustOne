package com.example.justone.translator.model

import com.example.foundation.network.ResourceState

data class WordTranslatorState(
    val translation: ResourceState<String> = ResourceState.Loading
)
