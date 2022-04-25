package com.example.justone.translator.model

sealed interface WordTranslatorAction {
    data class TranslateWord(val word: String) : WordTranslatorAction
}
