package com.setruth.jetpackcomposemviframework.ui.screen.main.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.navigation.NavOptions
import com.setruth.jetpackcomposemviframework.config.APPRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun UserView(snackBarState: SnackbarHostState, appNavCtr: NavHostController,) {
    val scope= rememberCoroutineScope()

    Box (Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface),contentAlignment = Alignment.Center){
        Button(onClick = {
            appNavCtr.navigate(
                APPRoute.LOGIN_VIEW,
                NavOptions.Builder().setPopUpTo(APPRoute.MAIN_VIEW, true).build()
            )
            scope.launch(Dispatchers.IO) {
                snackBarState.showSnackbar("用户模块发送了一条消息")
            }
        }) {
            Text(text = "退出登录")
        }
    }
}