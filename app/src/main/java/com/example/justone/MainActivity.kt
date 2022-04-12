package com.example.justone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.justone.ui.theme.JustOneTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JustOneTheme {
                val viewModel: WordGeneratorViewModel = hiltViewModel()
                val state by viewModel.collectAsState()
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Greeting(name = state.words.joinToString())
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello\n $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JustOneTheme {
        Greeting("Android")
    }
}
