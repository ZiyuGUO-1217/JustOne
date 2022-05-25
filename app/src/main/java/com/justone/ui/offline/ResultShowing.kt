package com.justone.ui.offline

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.justone.ui.theme.Green400
import com.justone.ui.theme.Red400

@Composable
fun ResultShowing(dialogWidth: Int, keyword: String, isCorrect: Boolean, setDialogState: (DialogState) -> Unit) {
    val size = with(LocalDensity.current) { dialogWidth.toDp() }
    val result = if (isCorrect) "Bingo!" else "So sad :("
    val resultColor = if (isCorrect) Green400 else Red400
    val onDoneClick = { setDialogState(DialogState.HIDE) }

    Box(
        modifier = Modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = result,
                modifier = Modifier.padding(vertical = 16.dp),
                fontSize = 88.sp,
                color = resultColor,
                fontWeight = FontWeight.ExtraBold
            )
            Text(text = "Keyword is: $keyword", fontSize = 24.sp)
            Button(onClick = onDoneClick, modifier = Modifier.padding(vertical = 32.dp)) {
                Text(text = "Next Round", fontSize = 32.sp)
            }
        }
    }
}
