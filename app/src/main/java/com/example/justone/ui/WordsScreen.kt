@file:OptIn(ExperimentalMaterialApi::class)

package com.example.justone.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.justone.WordGeneratorViewModel
import com.example.justone.collectAsState
import com.example.justone.model.WordGeneratorAction
import com.example.justone.model.WordGeneratorState
import com.example.justone.ui.theme.JustOneTheme
import com.example.justone.ui.theme.Primary
import com.example.justone.ui.theme.Secondary
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

@Composable
fun WordsScreen(viewModel: WordGeneratorViewModel = hiltViewModel()) {
    val state by viewModel.collectAsState()
    val actor = viewModel::dispatch

    ScreenContent(state, actor)
}

@Composable
private fun ScreenContent(
    state: WordGeneratorState,
    actor: (action: WordGeneratorAction) -> Unit
) {
    val onGenerateClick = { actor(WordGeneratorAction.GenerateWords) }
    val onWordClick = { }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomGenerateButton(onGenerateClick) },
        backgroundColor = MaterialTheme.colors.background
    ) {
        WordList(
            modifier = Modifier.padding(it),
            words = state.words,
            wordsNumber = state.wordsNumber,
            isLoading = state.isLoading,
            onWordClick = onWordClick
        )
    }
}

@Composable
private fun BottomGenerateButton(onGenerateClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onGenerateClick() }
            .background(Primary),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "GENERATE",
            modifier = Modifier
                .padding(vertical = 24.dp)
                .fillMaxWidth(),
            fontSize = 27.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun WordList(
    modifier: Modifier,
    words: List<String>,
    wordsNumber: Int,
    isLoading: Boolean,
    onWordClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        for (index in 0 until wordsNumber) {
            val word = "".takeIf { words.isEmpty() } ?: words[index]
            val highlight = PlaceholderHighlight.shimmer(Color.White).takeIf { isLoading }
            WordText(word, highlight, onWordClick)
        }
    }
}

@Composable
private fun WordText(
    word: String,
    highlight: PlaceholderHighlight?,
    onWordClick: () -> Unit
) {
    val color = Secondary
    val shape = RoundedCornerShape(32.dp)
    val modifier = if (word.isBlank()) Modifier.width(256.dp) else Modifier

    Card(
        onClick = { onWordClick() },
        modifier = modifier.placeholder(word.isBlank(), color, shape, highlight),
        shape = shape,
        backgroundColor = color,
        elevation = 4.dp
    ) {
        Text(
            text = word,
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp),
            fontSize = 31.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WordsScreenPreview() {
    val words = listOf("Modifier", "Preview", "Composable", "Content", "background")
    val wordsNumber = words.size
    JustOneTheme {
        ScreenContent(WordGeneratorState(emptyList(), wordsNumber)) {}
    }
}
