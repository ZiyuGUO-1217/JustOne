package com.justone.domain.usecases

import com.justone.domain.utils.ClueUtils
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class DeduplicateClueUseCaseTest {
    private val deduplicateClue = DeduplicateClueUseCase()

    @Before
    fun setUp() {
        mockkObject(ClueUtils)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun givenClueListAndKeyword_whenDeduplicateClue_thenClueUtilsMethodCalledAndReturnItsValue() {
        val clueList = listOf("clue1", "clue2", "clue3")
        val keyword = "keyword"
        every { ClueUtils.checkClueValidation(clueList, keyword) } returns clueList
        every { ClueUtils.deduplicateClues(clueList) } returns clueList

        val result = deduplicateClue(clueList, keyword)

        verify { ClueUtils.checkClueValidation(clueList, keyword) }
        verify { ClueUtils.deduplicateClues(clueList) }
        result shouldBe clueList
    }
}

