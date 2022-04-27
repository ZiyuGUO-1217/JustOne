package com.example.justone.model

sealed interface JustOneAction {
    object GenerateWords : JustOneAction
    data class TranslateWord(val word: String) :JustOneAction
}
