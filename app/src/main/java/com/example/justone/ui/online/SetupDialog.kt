package com.example.justone.ui.online

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.justone.LocalJustOneActor
import com.example.justone.model.JustOneAction
import com.example.justone.ui.widgets.DialogContainer
import com.example.justone.ui.widgets.FilledButton
import com.example.justone.ui.widgets.TextInputField

@Composable
fun SetupDialog() {
    val actor = LocalJustOneActor.current
    var roomId by remember { mutableStateOf("") }
    var playerId by remember { mutableStateOf("") }

    val onSetupClick = {
        actor(JustOneAction.SetupOnlineGame(roomId, playerId))
    }

    DialogContainer {
        Row(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
        ) {
            TextInputField(Modifier.weight(1f), roomId) {
                roomId = it
            }
            Spacer(modifier = Modifier.width(8.dp))
            TextInputField(Modifier.weight(1f), playerId) {
                playerId = it
            }
            Spacer(modifier = Modifier.width(8.dp))
            FilledButton(text = "Submit", onSetupClick)
        }
    }
}