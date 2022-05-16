package com.justone.ui.home

import androidx.core.text.isDigitsOnly

data class HomeState(
    val clueTimer: String,
    val guessTimer: String
) {
    val isValidClueTimer: Boolean = isValidTimer(clueTimer)
    val isValidGuessTimer: Boolean = isValidTimer(guessTimer)

    private fun isValidTimer(timer: String) = with(timer.trim()) {
        isDigitsOnly() && isNotBlank()
    }
}
