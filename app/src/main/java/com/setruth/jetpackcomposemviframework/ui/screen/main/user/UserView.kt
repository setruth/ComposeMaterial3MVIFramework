package com.setruth.jetpackcomposemviframework.ui.screen.main.user

import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun UserView(snackBarState: SnackbarHostState) {
    val scope= rememberCoroutineScope()
    Button(onClick = {
        scope.launch(Dispatchers.IO) {
            snackBarState.showSnackbar("用户模块发送了一条消息")
        }
    }) {
        Text(text = "提示")       
    }
}