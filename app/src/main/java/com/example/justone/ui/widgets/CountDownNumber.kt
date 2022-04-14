package com.example.justone.ui.widgets

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun CountDownNumber(leftTime: Int, countDownNumberColor: Color) {
    Text(
        text = leftTime.toString(),
        fontSize = 120.sp,
        fontWeight = FontWeight.ExtraBold,
        color = countDownNumberColor
    )
}
