package com.example.justone.model

import com.example.justone.data.JustOneRepository
import com.example.justone.data.ble.JustOneAdapter
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import org.junit.Test

class JustOneViewModelTest {
    private val mockRepository: JustOneRepository = mockk()
    private val mockAdapter: JustOneAdapter = mockk()
    private val viewModel = JustOneViewModel(mockRepository, mockAdapter)

    @Test
    fun givenThreeSameClues_whenDeduplicateClues_thenCluesShouldBeEmpty() {
        val clue = "clue"
        viewModel.dispatch(JustOneAction.SubmitClue(clue))
        viewModel.dispatch(JustOneAction.SubmitClue(clue))
        viewModel.dispatch(JustOneAction.SubmitClue(clue))

        viewModel.dispatch(JustOneAction.DeduplicateClue)

        viewModel.state.clues shouldBe emptyList()
    }

    @Test
    fun givenThreeDifferentClues_whenDeduplicateClues_thenCluesSizeShouldBeThree() {
        viewModel.dispatch(JustOneAction.SubmitClue("clue1"))
        viewModel.dispatch(JustOneAction.SubmitClue("clue2"))
        viewModel.dispatch(JustOneAction.SubmitClue("clue3"))

        viewModel.dispatch(JustOneAction.DeduplicateClue)

        viewModel.state.clues.size shouldBe 3
        viewModel.state.clues[1] shouldBe "clue2"
    }

    @Test
    fun givenThreeSimilarClues_whenDeduplicateClues_thenCluesSizeShouldBeZero() {
        viewModel.dispatch(JustOneAction.SubmitClue("clues"))
        viewModel.dispatch(JustOneAction.SubmitClue("clue"))
        viewModel.dispatch(JustOneAction.SubmitClue("cl ue"))

        viewModel.dispatch(JustOneAction.DeduplicateClue)

        viewModel.state.clues shouldBe emptyList()
    }

    @Test
    fun givenTwoSimilarCluesAndOneDifferent_whenDeduplicateClues_thenCluesSizeShouldBeOne() {
        viewModel.dispatch(JustOneAction.SubmitClue("Clue"))
        viewModel.dispatch(JustOneAction.SubmitClue("clue"))
        viewModel.dispatch(JustOneAction.SubmitClue("test"))

        viewModel.dispatch(JustOneAction.DeduplicateClue)

        viewModel.state.clues shouldBe listOf("test")
    }
}
