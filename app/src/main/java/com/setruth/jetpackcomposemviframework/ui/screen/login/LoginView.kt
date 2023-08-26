package com.setruth.jetpackcomposemviframework.ui.screen.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import com.setruth.jetpackcomposemviframework.constant.APPRoute


@Composable
fun LoginView(
    appNavController: NavHostController,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val loginPass by loginViewModel.loginPass.collectAsState()
    //监听login是否成功
    if (loginPass) {
        appNavController!!.navigate(
            APPRoute.MAIN_VIEW,
            NavOptions.Builder().setPopUpTo(APPRoute.LOGIN, true).build()
        )
    }
    View(loginViewModel)
}
@Composable
private fun View(loginViewModel: LoginViewModel){
    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            Button(onClick = { loginViewModel.handlerLoginIntent(LoginIntent.Login) }) {
                Text(text = "登录")
            }
        }
    }
}