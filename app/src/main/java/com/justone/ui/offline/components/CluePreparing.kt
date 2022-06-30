package com.justone.ui.offline.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.justone.ui.offline.LocalJustOneActor
import com.justone.ui.offline.OfflineAction
import com.justone.ui.widgets.FilledButton
import com.justone.ui.widgets.TextInputField

@Composable
fun CluePreparing(
    playersNumber: Int,
    submittedClues: List<String>,
) {
    val actor = LocalJustOneActor.current
    var inputClue by remember { mutableStateOf("") }
    val onSubmit = { clue: String -> actor(OfflineAction.SubmitClue(clue)) }
    val leftCluesNumber = (playersNumber - 1) - submittedClues.size

    Text(text = "Left clues: $leftCluesNumber")
    Row(
        modifier = Modifier
            .padding(horizontal = 32.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val modifier = Modifier.weight(1f)
        TextInputField(modifier, inputClue, "Input your clue here") {
            inputClue = it.replace('\n', ' ')
        }
        Spacer(modifier = Modifier.width(8.dp))
        FilledButton(text = "Submit", isEnable = inputClue.isNotBlank()) {
            onSubmit(inputClue)
            inputClue = ""
        }
    }
}
