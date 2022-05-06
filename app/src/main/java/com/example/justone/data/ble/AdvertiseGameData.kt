package com.example.justone.data.ble

import kotlinx.serialization.Serializable

@Serializable
data class AdvertiseGameData(val playerId: String) {
    var gameState: GameState = GameState.READY
    var playerCount: Int = 0
    var guessPlayer: String? = null
    var keyWord: String? = null
    var clueWord: String? = null
    var guessResult: Boolean? = null
}

enum class GameState {
    READY,
    KEY,
    CLUE,
    GUESS
}