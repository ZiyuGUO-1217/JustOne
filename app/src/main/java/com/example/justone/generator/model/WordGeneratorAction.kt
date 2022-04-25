package com.example.justone.generator.model

sealed interface WordGeneratorAction {
    object GenerateWords : WordGeneratorAction
}
