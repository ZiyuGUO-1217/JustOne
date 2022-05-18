package com.justone.ui.home

import app.cash.turbine.test
import com.justone.MainCoroutineRule
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    private lateinit var viewModel: HomeViewModel

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        viewModel = HomeViewModel()
    }

    @Test
    fun givenValidTimer_whenNavigateToOfflineScreen_thenSendCorrectNavigationEvent() = runTest {
        viewModel.dispatch(HomeAction.UpdateClueTimer(" 120 "))
        viewModel.dispatch(HomeAction.UpdateGuessTimer("090"))

        viewModel.event.test {
            viewModel.dispatch(HomeAction.NavigateToOfflineScreen)

            awaitItem() shouldBe HomeEvent.NavigateToOfflineScreen(120, 90)
        }
    }

    @Test
    fun givenValidTimer_whenNavigateToOnlineScreen_thenSendCorrectNavigationEvent() = runTest {
        viewModel.dispatch(HomeAction.UpdateClueTimer(" 00120 "))
        viewModel.dispatch(HomeAction.UpdateGuessTimer("090"))

        viewModel.event.test {
            viewModel.dispatch(HomeAction.NavigateToOnlineScreen)

            awaitItem() shouldBe HomeEvent.NavigateToOnlineScreen(120, 90)
        }
    }

    @Test
    fun givenInValidTimer_whenNavigateToOfflineScreen_thenSendValidationFailedEvent() = runTest {
        viewModel.dispatch(HomeAction.UpdateClueTimer(" 120 "))
        viewModel.dispatch(HomeAction.UpdateGuessTimer("0"))

        viewModel.event.test {
            viewModel.dispatch(HomeAction.NavigateToOfflineScreen)

            awaitItem() shouldBe HomeEvent.ValidationFailed
        }
    }

    @Test
    fun givenInValidTimer_whenNavigateToOnlineScreen_thenSendValidationFailedEvent() = runTest {
        viewModel.dispatch(HomeAction.UpdateClueTimer(" 000 "))
        viewModel.dispatch(HomeAction.UpdateGuessTimer("090"))

        viewModel.event.test {
            viewModel.dispatch(HomeAction.NavigateToOnlineScreen)

            awaitItem() shouldBe HomeEvent.ValidationFailed
        }
    }
}
