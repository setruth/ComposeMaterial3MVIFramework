package com.setruth.jetpackcomposemviframework.ui.screen.main

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun MainView(appNavController: NavHostController) {
    Surface {
        Text(text = "主页")
    }
}