package com.example.justone.ui.offline

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.justone.model.JustOneAction
import com.example.justone.ui.widgets.CountDownTimer
import com.example.justone.ui.widgets.FilledButton
import com.example.justone.ui.widgets.TextInputField

@Composable
fun CluePreparing(
    dialogWidth: Int,
    timer: Int,
    onCountDownFinished: () -> Unit
) {
    val actor = LocalJustOneActor.current
    var inputClue by remember { mutableStateOf("") }
    val onSubmit: (String) -> Unit = { clue -> actor(JustOneAction.SubmitClue(clue)) }

    CountDownTimer(timer, dialogWidth, onCountDownFinished)
    Row(
        modifier = Modifier
            .padding(horizontal = 32.dp)
            .fillMaxWidth()
    ) {
        val modifier = Modifier.weight(1f)
        TextInputField(modifier, inputClue) {
            inputClue = it.replace('\n', ' ')
        }
        Spacer(modifier = Modifier.width(8.dp))
        FilledButton(text = "Submit") {
            onSubmit(inputClue)
            inputClue = ""
        }
    }
}
