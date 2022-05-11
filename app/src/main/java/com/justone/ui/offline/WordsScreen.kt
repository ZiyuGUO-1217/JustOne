@file:OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)

package com.justone.ui.offline

import androidx.compose.animation.ExperimentalAnimationApi
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
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import com.justone.foundation.network.ResourceState
import com.justone.model.offline.JustOneOfflineState
import com.justone.model.offline.OfflineAction
import com.justone.ui.theme.JustOneTheme
import com.justone.ui.theme.Primary
import com.justone.ui.theme.Secondary
import com.justone.ui.widgets.DialogContainer

@Composable
fun WordsScreen(state: JustOneOfflineState) {
    val actor = LocalJustOneActor.current
    var clickedWord by remember { mutableStateOf("") }
    val (dialogState, setDialogState) = remember { mutableStateOf(DialogState.HIDE) }

    val onGenerateClick = {
        if (state.words != ResourceState.Loading) actor(OfflineAction.GenerateWords)
    }
    val onWordClick: (String) -> Unit = {
        setDialogState(DialogState.WORD)
        actor(OfflineAction.TranslateWord(it))
        clickedWord = it
    }
    val onClose = { setDialogState(DialogState.HIDE) }
    val onConfirm = {
        setDialogState(DialogState.CLUE)
        actor(OfflineAction.SelectKeyword(clickedWord))
    }
    val onCountDownFinished = {
        setDialogState(DialogState.GUESS)
        actor(OfflineAction.DeduplicateClue)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomGenerateButton(onGenerateClick) },
        backgroundColor = MaterialTheme.colors.background
    ) {
        WordList(
            modifier = Modifier.padding(it),
            words = state.words,
            wordsNumber = state.wordsNumber,
            onWordClick = onWordClick
        )
    }

    if (dialogState != DialogState.HIDE) {
        DialogContainer(onClose) { dialogWidth ->
            when (dialogState) {
                DialogState.WORD -> WordShowing(dialogWidth, clickedWord, state.translation, onClose, onConfirm)
                DialogState.CLUE -> CluePreparing(dialogWidth, state.timer, onCountDownFinished)
                DialogState.GUESS -> ClueShowing(dialogWidth, state.submittedClues, setDialogState)
            }
        }
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
    words: ResourceState<List<String>>,
    wordsNumber: Int,
    onWordClick: (String) -> Unit
) {
    val isLoading = words is ResourceState.Loading

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        for (index in 0 until wordsNumber) {
            val word = if (words is ResourceState.Success) words.data[index] else ""
            WordText(word, isLoading, onWordClick)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun WordText(
    word: String,
    isLoading: Boolean,
    onWordClick: (String) -> Unit
) {
    val color = Secondary
    val shape = RoundedCornerShape(32.dp)
    val isWordNotReady = word.isBlank()
    val modifier = if (isWordNotReady) Modifier.width(256.dp) else Modifier
    val highlight = PlaceholderHighlight.shimmer(Color.White).takeIf { isLoading }

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
    val state = JustOneOfflineState(
        words = ResourceState.Success(words),
        translation = translation,
        timer = 60,
        wordsNumber = wordsNumber
    )
    JustOneTheme {
        WordsScreen(state = state)
    }
}
