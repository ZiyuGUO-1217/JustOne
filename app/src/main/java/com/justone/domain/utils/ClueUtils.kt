package com.justone.domain.utils

object ClueUtils {

    fun deduplicateClues(cluesList: List<String>): List<String> {
        return cluesList.toMutableList().apply {
            cluesList.forEach { clue ->
                val duplicatedWeight = count { isDuplicated(it, clue) }
                if (duplicatedWeight != 1) removeAll { isDuplicated(it, clue) }
            }
        }
    }

    private fun isDuplicated(firstClue: String, secondClue: String): Boolean {
        val trimmedFirstClue = firstClue.replace(" ", "").lowercase()
        val trimmedSecondClue = secondClue.replace(" ", "").lowercase()
        return trimmedFirstClue.contains(trimmedSecondClue) || trimmedSecondClue.contains(trimmedFirstClue)
    }
}
