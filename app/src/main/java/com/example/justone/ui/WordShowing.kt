package com.example.justone.ui

import androidx.compose.animation.Crossfade
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foundation.network.ResourceState
import com.example.justone.ui.theme.Secondary
import com.example.justone.ui.widgets.FilledButton

@Composable
fun WordShowing(
    word: String,
    translation: ResourceState<String>,
    onClose: () -> Unit,
    onConfirm: () -> Unit,
    topContentModifier: Modifier = Modifier,
    bottomContentModifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = topContentModifier,
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = word,
                    modifier = Modifier.padding(vertical = 16.dp),
                    fontSize = 80.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Crossfade(targetState = translation) { translation ->
                    when (translation) {
                        is ResourceState.Success -> TranslationText(translation.data)
                        is ResourceState.Loading -> CircularProgressIndicator(color = Secondary)
                        else -> {
                            // Text -> Retry
                        }
                    }
                }
            }
        }
        Row(
            modifier = bottomContentModifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Drop this", fontSize = 32.sp, modifier = Modifier.clickable { onClose() })
            FilledButton("Confirm", onConfirm)
        }
    }
}

@Composable
private fun TranslationText(translation: String) {
    Text(
        text = translation,
        modifier = Modifier.padding(vertical = 16.dp),
        fontSize = 60.sp,
        fontWeight = FontWeight.Bold
    )
}
