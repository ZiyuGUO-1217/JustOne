package com.justone.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.justone.ui.widgets.FilledButton

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(navHostController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center)
        ) {
            FilledButton(text = "Online") { JustOneScreenRoute.Online.navigate(navHostController) }
            Spacer(modifier = Modifier.width(32.dp))
            FilledButton(text = "Offline") { JustOneScreenRoute.Offline.navigate(navHostController) }
        }
    }
}
