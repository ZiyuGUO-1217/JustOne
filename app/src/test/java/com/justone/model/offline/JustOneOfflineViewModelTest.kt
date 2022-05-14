package com.justone.model.offline

import app.cash.turbine.test
import com.justone.MainCoroutineRule
import com.justone.domain.JustOneUseCase
import com.justone.foundation.network.ResourceState
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
    private val mockUseCase = mockk<JustOneUseCase>(relaxed = true)
    private lateinit var viewModel: JustOneOfflineViewModel

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        viewModel = JustOneOfflineViewModel(mockUseCase)
    }

    @Test
    fun givenZeroPlayerNumber_whenRemovePlayer_thenPlayerNumberShouldBeZero() {
        viewModel.dispatch(OfflineAction.RemovePlayer)

        viewModel.state.playersNumber shouldBe 0
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
        every { mockUseCase.isValidPlayerNumber(0) } returns false

        viewModel.events.test {
            viewModel.dispatch(OfflineAction.GenerateWords)

            awaitItem() shouldBe OfflineEvent.InvalidPlayerNumber
            viewModel.state.words shouldBe ResourceState.Empty
        }
    }
}
