package com.justone.data.ble

import kotlinx.serialization.Serializable

@Serializable
data class AdvertiseGameData(val playerId: String) {
    var gameState: GameState = GameState.PREPARE
    var playerCount: Int = 0
    var guessPlayer: String? = null
    var keyWord: String? = null
    var clueWord: String? = null
    var guessResult: Boolean? = null
}

enum class GameState {
    PREPARE,
    READY,
    KEY,
    CLUE,
    GUESS
}
