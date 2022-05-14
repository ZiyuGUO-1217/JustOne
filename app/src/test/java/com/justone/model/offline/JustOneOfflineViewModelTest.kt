package com.justone.model.offline

import app.cash.turbine.test
import com.justone.MainCoroutineRule
import com.justone.domain.JustOneUseCase
import io.kotest.matchers.shouldBe
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
    fun givenThreePlayers_whenSubmitThreeClues_thenShouldSendClueCompleteEvent() = runTest {
        viewModel.dispatch(OfflineAction.AddPlayer)
        viewModel.dispatch(OfflineAction.AddPlayer)
        viewModel.dispatch(OfflineAction.AddPlayer)

        viewModel.events.test {
            viewModel.dispatch(OfflineAction.SubmitClue("1"))
            viewModel.dispatch(OfflineAction.SubmitClue("2"))
            viewModel.dispatch(OfflineAction.SubmitClue("3"))

            awaitItem() shouldBe OfflineEvent.ClueComplete
        }
    }
}
