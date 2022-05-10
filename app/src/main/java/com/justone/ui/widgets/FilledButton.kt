package com.justone.ui.widgets

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.justone.ui.theme.Primary

@Composable
fun FilledButton(text: String, onConfirm: () -> Unit) {
    Button(
        onClick = { onConfirm() },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Primary,
            contentColor = Color.White
        ),
    ) {
        Text(text = text, fontSize = 32.sp)
    }
}
