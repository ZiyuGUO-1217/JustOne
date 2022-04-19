package com.example.justone.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.justone.ui.theme.PrimaryDark
import com.example.justone.ui.theme.Secondary
import com.example.justone.ui.theme.SecondaryDark
import com.example.justone.ui.widgets.CountDownTimer
import com.example.justone.ui.widgets.FilledButton

@Composable
fun CluePreparing(dialogWidth: Int, timer: Int, bottomContentModifier: Modifier = Modifier) {
    var inputClue by remember { mutableStateOf("") }
    val onCountDownFinished = {}
    val onConfirm = {}

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CountDownTimer(timer, dialogWidth, onCountDownFinished)
        Row(modifier = bottomContentModifier) {
            val modifier = Modifier.weight(1f)
            ClueInputField(modifier, inputClue) {
                inputClue = it.replace('\n', ' ')
            }
            Spacer(modifier = Modifier.width(8.dp))
            FilledButton(text = "Submit", onConfirm = onConfirm)
        }
    }
}

@Composable
private fun ClueInputField(modifier: Modifier, inputClue: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = inputClue,
        onValueChange = onValueChange,
        modifier = modifier,
        textStyle = TextStyle(color = PrimaryDark, fontSize = 24.sp, fontWeight = FontWeight.Bold),
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = Color.Transparent,
            cursorColor = Secondary,
            unfocusedBorderColor = Secondary,
            focusedBorderColor = SecondaryDark
        )
    )
}
