package com.example.justone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.example.justone.ui.WordsScreen
import com.example.justone.ui.theme.JustOneTheme
import dagger.hilt.android.AndroidEntryPoint

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
        WordsScreen()
    }
}
