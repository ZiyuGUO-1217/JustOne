package com.justone.ui.offline

sealed interface OfflineAction {
    object GenerateWords : OfflineAction
    data class TranslateWord(val word: String) : OfflineAction
    data class SubmitClue(val clue: String) : OfflineAction
    object DeduplicateClue : OfflineAction
    object AddPlayer : OfflineAction
    object RemovePlayer : OfflineAction
    data class CheckAnswer(val guess: String) : OfflineAction

    data class SelectKeyword(val keyword: String) : OfflineAction
}


