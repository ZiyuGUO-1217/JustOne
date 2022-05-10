package com.justone.model.online

sealed interface OnlineAction {
    object GenerateWords : OnlineAction
    data class TranslateWord(val word: String) : OnlineAction
    data class SubmitClue(val clue: String) : OnlineAction
    object DeduplicateClue : OnlineAction

    data class SetupOnlineGame(val roomId: String, val playerId: String) : OnlineAction
}
