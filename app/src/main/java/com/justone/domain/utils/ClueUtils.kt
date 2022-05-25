package com.justone.domain.utils

object ClueUtils {

    fun deduplicateClues(cluesList: List<String>): List<String> {
        return cluesList.toMutableList().apply {
            cluesList.forEach { clue ->
                val duplicatedWeight = count { isSameClue(it, clue) }
                if (duplicatedWeight != 1) removeAll { isSameClue(it, clue) }
            }
        }
    }

    fun checkClueValidation(clueList: List<String>, keyword: String): List<String> {
        return clueList.toMutableList().apply {
            removeIf { isSameClue(it, keyword) }
        }
    }

    fun isSameClue(firstClue: String, secondClue: String): Boolean {
        val trimmedFirstClue = firstClue.replace(" ", "").lowercase()
        val trimmedSecondClue = secondClue.replace(" ", "").lowercase()
        return trimmedFirstClue.contains(trimmedSecondClue) || trimmedSecondClue.contains(trimmedFirstClue)
    }
}
