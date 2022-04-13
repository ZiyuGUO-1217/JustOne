package com.example.justone.model

sealed interface WordGeneratorAction {
    object GenerateWords : WordGeneratorAction
}
