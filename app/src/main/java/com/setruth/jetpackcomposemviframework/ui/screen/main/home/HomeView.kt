package com.setruth.jetpackcomposemviframework.ui.screen.main.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.setruth.jetpackcomposemviframework.config.APPRoute
import kotlinx.coroutines.launch


@Composable
fun HomeView(snackBarState: SnackbarHostState, appNavController: NavHostController) {
    val scope = rememberCoroutineScope()
    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                scope.launch {
                    snackBarState.showSnackbar("主页发送的提示消息")
                }
            }) {
                Text(text = "展示一条测试提示消息")
            }
            Button(onClick = {
                appNavController.navigate(APPRoute.DETAIL_VIEW)
            }) {
                Text(text = "进入详情页")
            }
        }
    }
}