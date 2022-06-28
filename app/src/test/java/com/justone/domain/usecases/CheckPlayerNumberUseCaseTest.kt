package com.justone.domain.usecases

import io.kotest.matchers.shouldBe
import org.junit.Test

class CheckPlayerNumberUseCaseTest {
    private val checkPlayerNumberUseCase = CheckPlayerNumberUseCase()

    @Test
    fun givenPlayerNumberLargerThanRequirement_whenCheckPlayerNumber_thenReturnTure() {
        val playerNumber = CheckPlayerNumberUseCase.MINIMUM_PLAYER_NUMBER + 1

        val result = checkPlayerNumberUseCase(playerNumber)

        result shouldBe true
    }

    @Test
    fun givenPlayerNumberEqualsRequirement_whenCheckPlayerNumber_thenReturnTure() {
        val playerNumber = CheckPlayerNumberUseCase.MINIMUM_PLAYER_NUMBER

        val result = checkPlayerNumberUseCase(playerNumber)

        result shouldBe true
    }

    @Test
    fun givenPlayerNumberLessThanRequirement_whenCheckPlayerNumber_thenReturnFalse() {
        val playerNumber = CheckPlayerNumberUseCase.MINIMUM_PLAYER_NUMBER - 1

        val result = checkPlayerNumberUseCase(playerNumber)

        result shouldBe false
    }
}
