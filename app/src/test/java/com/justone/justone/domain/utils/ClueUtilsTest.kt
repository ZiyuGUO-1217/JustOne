package com.justone.justone.domain.utils

import com.justone.domain.utils.ClueUtils
import io.kotest.matchers.shouldBe
import org.junit.Test

class ClueUtilsTest {
    private val clueUtils = ClueUtils

    @Test
    fun givenThreeSameClues_whenDeduplicateClues_thenCluesShouldBeEmpty() {
        val clueList = listOf("clue", "clue", "clue")

        val deduplicateClues = clueUtils.deduplicateClues(clueList)

        deduplicateClues shouldBe emptyList()
    }

    @Test
    fun givenThreeDifferentClues_whenDeduplicateClues_thenCluesSizeShouldBeThree() {
        val clueList = listOf("clue1", "clue2", "clue3")

        val deduplicateClues = clueUtils.deduplicateClues(clueList)

        deduplicateClues.size shouldBe 3
        deduplicateClues[1] shouldBe "clue2"
    }

    @Test
    fun givenThreeSimilarClues_whenDeduplicateClues_thenCluesSizeShouldBeZero() {
        val clueList = listOf("clues", "clue", "cl ue")

        val deduplicateClues = clueUtils.deduplicateClues(clueList)

        deduplicateClues shouldBe emptyList()
    }

    @Test
    fun givenTwoSimilarCluesAndOneDifferent_whenDeduplicateClues_thenCluesSizeShouldBeOne() {
        val clueList = listOf("Clue", "clue", "test")

        val deduplicateClues = clueUtils.deduplicateClues(clueList)

        deduplicateClues shouldBe listOf("test")
    }
}
