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
class TranslateWordUseCaseTest {
    private val mockRepository = mockk<JustOneWordsRepository>()
    private val translateWord = TranslateWordUseCase(mockRepository)

    @Test
    fun givenKeywordAndApiSuccess_whenTranslateWord_thenRepoMethodCalledAndReturnSuccessValue() = runTest {
        val keyword = "keyword"
        coEvery { mockRepository.translateWord(keyword) } returns "关键字"

        val translation = translateWord(keyword)

        coVerify { mockRepository.translateWord(keyword) }
        translation shouldBe ApiResult.Success("关键字")
    }

    @Test
    fun givenKeywordAndApiError_whenTranslateWord_thenRepoMethodCalledAndReturnErrorValue() = runTest {
        val keyword = "keyword"
        coEvery { mockRepository.translateWord(keyword) } throws Exception()

        val translation = translateWord(keyword)

        coVerify { mockRepository.translateWord(keyword) }
        translation shouldBe ApiResult.Error(ErrorType.ServerError)
    }

}
