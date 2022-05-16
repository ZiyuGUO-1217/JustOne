package com.justone.ui.home

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.justone.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class HomeViewModelTest {
    private lateinit var viewModel: HomeViewModel

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        viewModel = HomeViewModel()
    }

    //TODO: separate isDigitalOnly and isEmpty from data class
//    @Test
//    fun givenValidTimer_whenNavigateToOfflineScreen_thenSendCorrectNavigationEvent() = runTest {
//        viewModel.dispatch(HomeAction.UpdateClueTimer(" 120 "))
//        viewModel.dispatch(HomeAction.UpdateGuessTimer("090"))
//
//        viewModel.event.test {
//            viewModel.dispatch(HomeAction.NavigateToOfflineScreen)
//
//            awaitItem() shouldBe HomeEvent.NavigateToOfflineScreen(120, 90)
//        }
//    }

//    @Test
//    fun givenValidTimer_whenNavigateToOnlineScreen_thenSendCorrectNavigationEvent() = runTest {
//        viewModel.dispatch(HomeAction.UpdateClueTimer(" 00120 "))
//        viewModel.dispatch(HomeAction.UpdateGuessTimer("090"))
//
//        viewModel.event.test {
//            viewModel.dispatch(HomeAction.NavigateToOnlineScreen)
//
//            awaitItem() shouldBe HomeEvent.NavigateToOnlineScreen(120, 90)
//        }
//    }
}
