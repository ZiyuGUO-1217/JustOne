@file:OptIn(ExperimentalComposeUiApi::class)

package com.justone.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

private const val DIALOG_FRACTION = 0.82f

@Composable
fun DialogContainer(
    onClose: () -> Unit = {},
    dismissOnBackPress: Boolean = true,
    content: @Composable ColumnScope.(Int) -> Unit
) {
    var dialogWidth by remember { mutableStateOf(0) }

    Dialog(
        onDismissRequest = { onClose() },
        properties = DialogProperties(
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(DIALOG_FRACTION)
                .padding(32.dp)
                .onSizeChanged { dialogWidth = it.width },
            elevation = 4.dp,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                this.content(dialogWidth)
            }
        }
    }
}
