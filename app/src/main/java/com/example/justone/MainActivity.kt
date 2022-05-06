package com.example.justone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.justone.model.JustOneAction
import com.example.justone.model.JustOneViewModel
import com.example.justone.ui.WordsScreen
import com.example.justone.ui.theme.JustOneTheme
import dagger.hilt.android.AndroidEntryPoint

val LocalJustOneActor = compositionLocalOf<(JustOneAction) -> Unit> {
    error("on JustOneActor provided")
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JustOneTheme {
                Content()
            }
        }
    }

    @Composable
    private fun Content() {
        val viewModel: JustOneViewModel = hiltViewModel()
        val state by viewModel.flow.collectAsState()
        val actor = viewModel::dispatch

        CompositionLocalProvider(LocalJustOneActor provides actor) {
            WordsScreen(state)
        }
    }
}
