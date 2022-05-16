package com.justone.ui.widgets

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.justone.ui.theme.PrimaryDark
import com.justone.ui.theme.Secondary
import com.justone.ui.theme.SecondaryDark

@Composable
fun TextInputFieldWithErrorMessage(
    modifier: Modifier = Modifier,
    inputValue: String,
    placeHolder: String? = null,
    label: String? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.animateContentSize()) {
        TextInputField(modifier, inputValue, placeHolder, label, isError, onValueChange)
        if (isError) Text(text = errorMessage ?: "", modifier = Modifier.wrapContentHeight(), color = Color.Red)
    }
}

@Composable
fun TextInputField(
    modifier: Modifier = Modifier,
    inputValue: String,
    placeHolder: String? = null,
    label: String? = null,
    isError: Boolean = false,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = inputValue,
        onValueChange = onValueChange,
        modifier = modifier,
        textStyle = TextStyle(color = PrimaryDark, fontSize = 24.sp, fontWeight = FontWeight.Bold),
        placeholder = { placeHolder?.let { Text(text = it) } },
        label = { label?.let { Text(text = it) } },
        isError = isError,
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = Color.Transparent,
            cursorColor = Secondary,
            unfocusedBorderColor = Secondary,
            focusedBorderColor = SecondaryDark
        )
    )
}
