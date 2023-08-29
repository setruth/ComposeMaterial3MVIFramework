package com.setruth.jetpackcomposemviframework.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.setruth.jetpackcomposemviframework.constant.APPRoute
import com.setruth.jetpackcomposemviframework.constant.KVKey
import com.setruth.jetpackcomposemviframework.constant.kv
import com.setruth.jetpackcomposemviframework.ui.screen.guide.GuideView
import com.setruth.jetpackcomposemviframework.ui.screen.login.LoginView
import com.setruth.jetpackcomposemviframework.ui.screen.main.MainView
import com.setruth.jetpackcomposemviframework.ui.theme.APPTheme
import com.tencent.mmkv.MMKV
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MMKV.initialize(this);
        setContent {
            APPTheme {
                val startRoute = if (kv.getBoolean(KVKey.FIRST_ENTER, false)) {
                    APPRoute.LOGIN
                } else {
                    APPRoute.Guide
                }
                val appNavController = rememberNavController()
                NavHost(navController = appNavController, startDestination =startRoute) {
                    composable(APPRoute.LOGIN) {
                        LoginView(appNavController)
                    }
                    composable(APPRoute.MAIN_VIEW) {
                        MainView(appNavController)
                    }
                    composable(APPRoute.Guide) {
                        GuideView(appNavController)
                    }
                }
            }
        }
    }
}
