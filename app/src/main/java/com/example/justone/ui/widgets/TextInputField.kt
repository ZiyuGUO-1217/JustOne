package com.example.justone.ui.widgets

import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.justone.ui.theme.PrimaryDark
import com.example.justone.ui.theme.Secondary
import com.example.justone.ui.theme.SecondaryDark

@Composable
fun TextInputField(modifier: Modifier, inputClue: String, onValueChange: (String) -> Unit) {
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
