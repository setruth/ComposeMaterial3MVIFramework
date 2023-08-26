package com.setruth.jetpackcomposemviframework.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.setruth.jetpackcomposemviframework.constant.APPRoute
import com.setruth.jetpackcomposemviframework.ui.screen.login.LoginView
import com.setruth.jetpackcomposemviframework.ui.screen.main.MainView
import com.setruth.jetpackcomposemviframework.ui.theme.APPTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            APPTheme {
                val appNavController = rememberNavController()
                NavHost(navController = appNavController, startDestination = APPRoute.LOGIN) {
                    composable(APPRoute.LOGIN) {
                        LoginView(appNavController)
                    }
                    composable(APPRoute.MAIN_VIEW) {
                        MainView(appNavController)
                    }
                }
            }
        }
    }
}
