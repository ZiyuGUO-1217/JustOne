package com.justone.ui.offline

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.justone.MainCoroutineRule
import com.justone.domain.usecases.CheckAnswerUseCase
import com.justone.domain.usecases.CheckPlayerNumberUseCase
import com.justone.domain.usecases.DeduplicateClueUseCase
import com.justone.domain.usecases.GenerateWordsUseCase
import com.justone.domain.usecases.TranslateWordUseCase
import com.justone.foundation.network.ResourceState
import com.justone.ui.JustOneScreenRoute
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class JustOneOfflineViewModelTest {
    private val mockSavedStateHandle = mockk<SavedStateHandle>(relaxed = true)
    private val mockGenerateWords = mockk<GenerateWordsUseCase>(relaxed = true)
    private val mockTranslateWord = mockk<TranslateWordUseCase>(relaxed = true)
    private val mockDeduplicateClue = mockk<DeduplicateClueUseCase>(relaxed = true)
    private val mockCheckPlayerNumber = mockk<CheckPlayerNumberUseCase>(relaxed = true)
    private val mockCheckAnswer = mockk<CheckAnswerUseCase>(relaxed = true)
    private lateinit var viewModel: JustOneOfflineViewModel

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        every { mockSavedStateHandle.get<Int>(JustOneScreenRoute.Offline.KEY_CLUE_TIMER) } returns 120
        every { mockSavedStateHandle.get<Int>(JustOneScreenRoute.Offline.KEY_GUESS_TIMER) } returns 90

        viewModel = JustOneOfflineViewModel(
            mockSavedStateHandle,
            mockGenerateWords,
            mockTranslateWord,
            mockDeduplicateClue,
            mockCheckPlayerNumber,
            mockCheckAnswer
        )
    }

    @Test
    fun givenZeroPlayerNumber_whenRemovePlayer_thenPlayerNumberShouldBeZero() {
        every { mockCheckPlayerNumber(-1) } returns false

        viewModel.dispatch(OfflineAction.RemovePlayer)

        viewModel.flow.value.playersNumber shouldBe 0
    }

    @Test
    fun givenThreePlayers_whenSubmitTwoClues_thenShouldSendClueCompleteEvent() = runTest {
        viewModel.dispatch(OfflineAction.AddPlayer)
        viewModel.dispatch(OfflineAction.AddPlayer)
        viewModel.dispatch(OfflineAction.AddPlayer)

        viewModel.events.test {
            viewModel.dispatch(OfflineAction.SubmitClue("1"))
            viewModel.dispatch(OfflineAction.SubmitClue("2"))

            awaitItem() shouldBe OfflineEvent.ClueComplete
        }
    }

    @Test
    fun givenZeroPlayers_whenGenerateWords_thenShouldSendInvalidPlayerNumberEvent() = runTest {
        every { mockCheckPlayerNumber(0) } returns false

        viewModel.events.test {
            viewModel.dispatch(OfflineAction.GenerateWords)

            awaitItem() shouldBe OfflineEvent.InvalidPlayerNumber
            viewModel.flow.value.words shouldBe ResourceState.Empty
        }
    }

    @Test
    fun givenCorrectAnswer_whenCheckAnswer_thenShouldSendCorrectAnswerEvent() = runTest {
        val keyword = "keyword"
        val answer = "keyword"
        every { mockCheckAnswer(keyword, answer) } returns true
        viewModel.dispatch(OfflineAction.SelectKeyword(keyword))

        viewModel.events.test {
            viewModel.dispatch(OfflineAction.CheckAnswer(answer))

            awaitItem() shouldBe OfflineEvent.CorrectAnswer
            viewModel.flow.value.isAnswerCorrect shouldBe true
        }
    }

    @Test
    fun givenWrongAnswer_whenCheckAnswer_thenShouldSendWrongAnswerEvent() = runTest {
        val keyword = "keyword"
        val answer = "answer"
        every { mockCheckAnswer(keyword, answer) } returns false
        viewModel.dispatch(OfflineAction.SelectKeyword(keyword))

        viewModel.events.test {
            viewModel.dispatch(OfflineAction.CheckAnswer(answer))

            awaitItem() shouldBe OfflineEvent.WrongAnswer
            viewModel.flow.value.isAnswerCorrect shouldBe false
        }
    }
}
