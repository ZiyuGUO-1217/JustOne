package com.example.justone.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.justone.ui.widgets.CountDownTimer
import com.example.justone.ui.widgets.FilledButton

@Composable
fun HintPreparing(dialogWidth: Int, timer: Int, bottomContentModifier: Modifier) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CountDownTimer(timer, dialogWidth) {}
        Row(modifier = bottomContentModifier) {
            FilledButton(text = "Submit") {}
        }
    }
}
