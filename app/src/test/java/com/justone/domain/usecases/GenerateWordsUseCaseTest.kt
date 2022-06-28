package com.justone.domain.usecases

import com.justone.data.JustOneWordsRepository
import com.justone.foundation.network.ApiResult
import com.justone.foundation.network.ErrorType
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GenerateWordsUseCaseTest {
    private val mockRepository = mockk<JustOneWordsRepository>()
    private val generateWords = GenerateWordsUseCase(mockRepository)

    @Test
    fun givenApiSuccess_whenGenerateWords_thenRepoMethodCalledAndReturnSuccessValue() = runTest {
        val words = listOf("word1","word2","word3","word4","word5")
        coEvery { mockRepository.getRandomWordList(5) } returns words

        val wordList = generateWords(Unit)

        coVerify { mockRepository.getRandomWordList(5) }
        wordList shouldBe ApiResult.Success(words)
    }

    @Test
    fun givenApiError_whenGenerateWords_thenRepoMethodCalledAndReturnErrorValue() = runTest {
        coEvery { mockRepository.getRandomWordList(5) } throws Exception()

        val wordList = generateWords(Unit)

        coVerify { mockRepository.getRandomWordList(5) }
        wordList shouldBe ApiResult.Error(ErrorType.ServerError)
    }

}

