package com.example.justone.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.foundation.network.ResourceState

private const val DIALOG_FRACTION = 0.82f

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WordDialog(
    word: String,
    translation: ResourceState<String>,
    timer: Int,
    dialogState: DialogState,
    onClose: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    var dialogWidth by remember { mutableStateOf(0) }

    Dialog(
        onDismissRequest = { onClose() },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(DIALOG_FRACTION)
                .padding(32.dp)
                .onSizeChanged { dialogWidth = it.width },
            elevation = 4.dp
        ) {
            val height = with(LocalDensity.current) { dialogWidth.toDp() }
            val topContentModifier = Modifier
                .fillMaxWidth()
                .height(height)
                .padding(horizontal = 32.dp, vertical = 16.dp)

            when (dialogState) {
                DialogState.WORD -> {
                    WordShowing(word, translation, onClose, onConfirm, topContentModifier)
                }
                DialogState.CLUE -> {
                    CluePreparing(dialogWidth = dialogWidth, timer = timer, onCountDownFinished = onClose)
                }
            }
        }
    }
}
