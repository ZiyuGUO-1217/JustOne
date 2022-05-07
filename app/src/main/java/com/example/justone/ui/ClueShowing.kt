package com.example.justone.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.justone.ui.theme.Secondary
import com.example.justone.ui.widgets.FilledButton
import com.example.justone.ui.widgets.TextInputField

@Composable
fun ClueShowing(dialogWidth: Int, clues: List<String>, setDialogState: (DialogState) -> Unit) {
    var inputAnswer by remember { mutableStateOf("") }
    val onSubmit: (String) -> Unit = { setDialogState(DialogState.HIDE) }

    val size = with(LocalDensity.current) { dialogWidth.toDp() }
    Column(
        modifier = Modifier.size(size),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        clues.forEach { clue ->
            Text(text = clue, fontSize = 56.sp, color = Secondary)
        }
    }
    Row(
        modifier = Modifier
            .padding(horizontal = 32.dp)
            .fillMaxWidth()
    ) {
        val modifier = Modifier.weight(1f)
        TextInputField(modifier, inputAnswer) {
            inputAnswer = it.replace('\n', ' ')
        }
        Spacer(modifier = Modifier.width(8.dp))
        FilledButton(text = "Submit") { onSubmit(inputAnswer) }
    }
}
