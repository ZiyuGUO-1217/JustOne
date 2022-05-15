package com.justone.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavHostController
import com.justone.domain.JustOneUseCase
import com.justone.ui.widgets.FilledButton
import com.justone.ui.widgets.TextInputField

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(navHostController: NavHostController) {
    val clueTimer = remember {
        mutableStateOf(JustOneUseCase.DEFAULT_CLUE_TIMER.toString())
    }
    val guessTimer = remember {
        mutableStateOf(JustOneUseCase.DEFAULT_GUESS_TIMER.toString())
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            TextInputField(inputValue = clueTimer.value, label = "Time for clue") { timer ->
                if (timer.isDigitsOnly()) clueTimer.value = timer
            }
            Spacer(modifier = Modifier.height(32.dp))
            TextInputField(inputValue = guessTimer.value, label = "Time for guess") { timer ->
                if (timer.isDigitsOnly()) guessTimer.value = timer
            }
            Spacer(modifier = Modifier.height(48.dp))
            Row(
                modifier = Modifier.wrapContentSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledButton(text = "Online") {
                    JustOneScreenRoute.Online.navigate(
                        navHostController = navHostController,
                        clueTimer = clueTimer.value.toInt(),
                        guessTimer = guessTimer.value.toInt()
                    )
                }
                Spacer(modifier = Modifier.width(32.dp))
                FilledButton(text = "Offline") {
                    JustOneScreenRoute.Offline.navigate(
                        navHostController = navHostController,
                        clueTimer = clueTimer.value.toInt(),
                        guessTimer = guessTimer.value.toInt())
                }
            }
        }
    }
}
