package com.justone.model.offline

sealed interface OfflineAction {
    object GenerateWords : OfflineAction
    data class TranslateWord(val word: String) : OfflineAction
    data class SubmitClue(val clue: String) : OfflineAction
    object DeduplicateClue : OfflineAction

    data class SelectKeyword(val keyword: String) : OfflineAction
}


