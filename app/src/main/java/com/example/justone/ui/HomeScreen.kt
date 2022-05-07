package com.example.justone.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.justone.LocalJustOneActor
import com.example.justone.model.JustOneAction
import com.example.justone.model.JustOneState
import com.example.justone.ui.offline.WordsScreen
import com.example.justone.ui.online.OnlineScreen
import com.example.justone.ui.widgets.FilledButton

@Composable
fun HomeScreen(state: JustOneState) {
    val actor = LocalJustOneActor.current
    val onModeSelect: (Boolean) -> Unit = {
        actor(JustOneAction.SelectGameMode(it))
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when(state.onlineMode) {
            true -> OnlineScreen(state)
            false -> WordsScreen(state)
            else -> {
                Row(modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.Center)) {
                    FilledButton(text = "Online") { onModeSelect(true) }
                    Spacer(modifier = Modifier.width(32.dp))
                    FilledButton(text = "Offline") { onModeSelect(false) }
                }
            }
        }
    }
}