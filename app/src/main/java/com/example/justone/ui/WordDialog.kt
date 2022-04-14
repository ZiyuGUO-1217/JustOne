package com.example.justone.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.justone.ui.theme.Primary
import com.example.justone.ui.widgets.CountDownTimer

private const val DIALOG_FRACTION = 0.82f

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WordDialog(
    word: String,
    timer: Int,
    dialogState: DialogState,
    onClose: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    if (dialogState != DialogState.HIDE) {
        var dialogWidth by remember { mutableStateOf(0) }

        Dialog(
            onDismissRequest = { onClose() },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(DIALOG_FRACTION)
                    .padding(32.dp)
                    .onSizeChanged { dialogWidth = it.width },
                elevation = 4.dp
            ) {
                when (dialogState) {
                    DialogState.WORD -> WordShowing(word, onClose, onConfirm)
                    DialogState.COUNT_DOWN -> CountDownShowing(dialogWidth, timer)
                }
            }
        }
    }
}

@Composable
fun CountDownShowing(dialogWidth: Int, timer: Int) {
    CountDownTimer(timer, dialogWidth) {}
}

@Composable
private fun WordShowing(word: String, onClose: () -> Unit, onConfirm: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 72.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = word,
                modifier = Modifier.padding(vertical = 16.dp),
                fontSize = 80.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
        Row(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Drop this", fontSize = 32.sp, modifier = Modifier.clickable { onClose() })
            Button(
                onClick = { onConfirm() },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Primary,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Confirm", fontSize = 32.sp)
            }
        }
    }
}
