package com.example.justone.ui.widgets

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.justone.ui.theme.Primary
import com.example.justone.ui.theme.SecondaryDark

@Composable
fun CountDownNumber(leftTime: Int) {
    var countDownNumberColor by remember { mutableStateOf(Primary) }
    if (leftTime <= 10) countDownNumberColor = SecondaryDark

    val hundreds = leftTime / 100
    val tens = (leftTime % 100) / 10
    val ones = leftTime % 10

    Row {
        if (hundreds != 0) AnimatedText(hundreds, countDownNumberColor)
        if (tens != 0 || hundreds != 0) AnimatedText(tens, countDownNumberColor)
        AnimatedText(ones, countDownNumberColor)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun AnimatedText(number: Int, countDownNumberColor: Color) {
    val contentTransform = slideInVertically { height -> height } + fadeIn() with
            slideOutVertically { height -> -height } + fadeOut()
    AnimatedContent(
        targetState = number,
        transitionSpec = {
            contentTransform.using(SizeTransform(clip = true))
        }
    ) { targetNumber ->
        Text(
            text = targetNumber.toString(),
            fontSize = 120.sp,
            fontWeight = FontWeight.ExtraBold,
            color = countDownNumberColor
        )
    }
}
