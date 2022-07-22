package com.justone.ui.online.components

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
import com.justone.ui.online.LocalOnlineActor
import com.justone.ui.online.OnlineAction
import com.justone.ui.widgets.DialogContainer
import com.justone.ui.widgets.FilledButton
import com.justone.ui.widgets.TextInputField

@Composable
fun SetupDialog(onClose: () -> Unit) {
    val actor = LocalOnlineActor.current
    var roomId by remember { mutableStateOf("") }
    var playerId by remember { mutableStateOf("") }

    val onSetupClick = {
        actor(OnlineAction.SetupOnlineGame(roomId, playerId))
    }

    DialogContainer(onClose, false) {
        Row(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
        ) {
            TextInputField(Modifier.weight(1f), roomId, "Room Id") {
                roomId = it
            }
            Spacer(modifier = Modifier.width(8.dp))
            TextInputField(Modifier.weight(1f), playerId, "Your player Id") {
                playerId = it
            }
            Spacer(modifier = Modifier.width(8.dp))
            FilledButton(text = "Submit") { onSetupClick() }
        }
    }
}
