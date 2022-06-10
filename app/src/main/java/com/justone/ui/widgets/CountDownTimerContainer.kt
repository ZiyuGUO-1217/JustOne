package com.justone.ui.widgets

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun CountDownTimerContainer(
    timer: Int,
    onCountDownFinished: () -> Unit,
    content: @Composable (Int) -> Unit
) {
    var countDownTimer by remember { mutableStateOf(timer) }
    val leftTime by animateIntAsState(
        targetValue = countDownTimer,
        animationSpec = tween(durationMillis = timer * 1000, easing = LinearEasing),
        finishedListener = {
            onCountDownFinished()
        }
    )

    content(leftTime)

    LaunchedEffect(countDownTimer) {
        countDownTimer = 0
    }
}
