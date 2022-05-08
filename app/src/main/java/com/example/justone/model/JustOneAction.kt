package com.example.justone.model

sealed interface JustOneAction {
    object GenerateWords : JustOneAction
    data class TranslateWord(val word: String) : JustOneAction
    object HideWords : JustOneAction
    data class SubmitClue(val clue: String) : JustOneAction
    object DeduplicateClue : JustOneAction

    sealed interface OnlineAction : JustOneAction {
        data class SetupOnlineGame(val roomId: String, val playerId: String) : OnlineAction
    }
}
