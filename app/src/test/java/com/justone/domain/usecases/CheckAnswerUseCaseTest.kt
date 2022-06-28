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

class CheckAnswerUseCaseTest {
    private val checkAnswer = CheckAnswerUseCase()

    @Before
    fun setUp() {
        mockkObject(ClueUtils)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun givenEmptyGuess_whenCheckAnswer_thenClueUtilMethodCalledAndReturnFalse() {
        val answer = "answer"
        val guess = ""
        every { ClueUtils.isSameClue(answer, guess) } returns true

        val result = checkAnswer(answer, guess)

        verify { ClueUtils.isSameClue("answer", "") }
        result shouldBe false
    }

    @Test
    fun givenRightGuess_whenCheckAnswer_thenClueUtilMethodCalledAndReturnTure() {
        val answer = "answer"
        val guess = "answer"
        every { ClueUtils.isSameClue(answer, guess) } returns true

        val result = checkAnswer(answer, guess)

        verify { ClueUtils.isSameClue("answer", "answer") }
        result shouldBe true
    }

    @Test
    fun givenWrongGuess_whenCheckAnswer_thenClueUtilMethodCalledAndReturnFalse() {
        val answer = "answer"
        val guess = "guess"
        every { ClueUtils.isSameClue(answer, guess) } returns false

        val result = checkAnswer(answer, guess)

        verify { ClueUtils.isSameClue("answer", "guess") }
        result shouldBe false
    }
}
