package com.example.justone.ui.online

import androidx.compose.runtime.Composable
import com.example.justone.model.JustOneState

@Composable
fun OnlineScreen(state: JustOneState) {
    if (state.onlineSetup) {

    } else {
        SetupDialog()
    }
}
