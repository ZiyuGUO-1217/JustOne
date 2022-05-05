package com.example.justone.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foundation.network.ResourceState
import com.example.justone.ui.theme.PrimaryLight
import com.example.justone.ui.theme.Secondary
import com.example.justone.ui.theme.SecondaryDark
import com.example.justone.ui.widgets.FilledButton
 
@Composable
fun WordShowing(
    word: String,
    translation: ResourceState<String>,
    onClose: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    val onNeedEditClick = {}

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text(
                    text = word,
                    modifier = Modifier.padding(vertical = 16.dp),
                    fontSize = 100.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                when (translation) {
                    is ResourceState.Success -> TranslationField(translation, onNeedEditClick)
                    is ResourceState.Loading -> CircularProgressIndicator(color = Secondary)
                    else -> {
                        // Text -> Retry
                    }
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Drop this", fontSize = 32.sp, modifier = Modifier.clickable { onClose() })
            FilledButton("Confirm", onConfirm)
        }
    }
}

@Composable
private fun TranslationField(
    translation: ResourceState.Success<String>,
    onNeedEditClick: () -> Unit
) {
    LocalConfiguration.current.densityDpi
    Text(
        text = translation.data,
        color = SecondaryDark,
        fontSize = 64.sp,
        fontWeight = FontWeight.Bold
    )
    Text(
        text = "Need edit?",
        modifier = Modifier.clickable { onNeedEditClick() },
        color = PrimaryLight,
        fontSize = 24.sp,
        textDecoration = TextDecoration.Underline
    )
}
