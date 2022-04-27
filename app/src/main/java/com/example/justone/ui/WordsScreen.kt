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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.foundation.network.ResourceState
import com.example.justone.model.JustOneAction
import com.example.justone.model.JustOneState
import com.example.justone.model.JustOneViewModel
import com.example.justone.ui.theme.JustOneTheme
import com.example.justone.ui.theme.Primary
import com.example.justone.ui.theme.Secondary
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

@Composable
fun WordsScreen() {
    val viewModel: JustOneViewModel = hiltViewModel()
    val state by viewModel.flow.collectAsState()
    val actor = viewModel::dispatch

    ScreenContent(state, actor)
}

@Composable
private fun ScreenContent(
    state: JustOneState,
    actor: (action: JustOneAction) -> Unit
) {
    val onGenerateClick = { actor(JustOneAction.GenerateWords) }
    var clickedWord by remember { mutableStateOf("") }
    var dialogState by remember { mutableStateOf(DialogState.HIDE) }
    val onWordClick: (String) -> Unit = {
        actor(JustOneAction.TranslateWord(it))
        dialogState = DialogState.WORD
        clickedWord = it
    }
    val onClose = { dialogState = DialogState.HIDE }
    val onConfirm = { dialogState = DialogState.CLUE }

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

    if (dialogState != DialogState.HIDE) {
        WordDialog(clickedWord, state.translation, state.timer, dialogState, onClose, onConfirm)
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
                .padding(vertical = 36.dp)
                .fillMaxWidth(),
            fontSize = 30.sp,
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
    onWordClick: (String) -> Unit
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun WordText(
    word: String,
    highlight: PlaceholderHighlight?,
    onWordClick: (String) -> Unit
) {
    val color = Secondary
    val shape = RoundedCornerShape(32.dp)
    val isWordNotReady = word.isBlank()
    val modifier = if (isWordNotReady) Modifier.width(256.dp) else Modifier

    Card(
        onClick = { if (isWordNotReady.not()) onWordClick(word) },
        modifier = modifier.placeholder(isWordNotReady, color, shape, highlight),
        shape = shape,
        backgroundColor = color,
        elevation = 4.dp
    ) {
        Text(
            text = word,
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp),
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WordsScreenPreview() {
    val words = listOf("Modifier", "Preview", "Composable", "Content", "background")
    val translation = ResourceState.Success("translation")
    val wordsNumber = words.size
    val state = JustOneState(words = words, translation = translation, wordsNumber = wordsNumber, timer = 60)
    JustOneTheme {
        ScreenContent(state = state, actor = {})
    }
}
