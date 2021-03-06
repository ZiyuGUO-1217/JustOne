package com.justone.ui.widgets

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.sp
import com.justone.ui.theme.Primary

@Composable
fun FilledButton(text: String, isEnable: Boolean = true, shouldClearFocus: Boolean = true, onConfirm: () -> Unit) {
    val focusManager = LocalFocusManager.current
    val colors = ButtonDefaults.buttonColors(
        backgroundColor = if (isEnable) Primary else Color.LightGray,
        contentColor = Color.White
    )
    Button(
        onClick = {
            if (shouldClearFocus) focusManager.clearFocus()
            if (isEnable) onConfirm()
        },
        colors = colors,
    ) {
        Text(text = text, fontSize = 32.sp)
    }
}
