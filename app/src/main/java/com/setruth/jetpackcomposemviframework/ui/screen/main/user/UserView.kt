package com.setruth.jetpackcomposemviframework.ui.screen.main.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import com.setruth.jetpackcomposemviframework.config.APPRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun UserView(
    snackBarState: SnackbarHostState,
    appNavCtr: NavHostController,
    userViewModel: UserViewModel= hiltViewModel()
) {
    val logoutState by userViewModel.logoutState.collectAsState()
    LaunchedEffect(logoutState){
        if (logoutState){
            appNavCtr.navigate(
                APPRoute.LOGIN_VIEW,
                NavOptions.Builder().setPopUpTo(APPRoute.MAIN_VIEW, true).build()
            )
        }
    }
    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = {
            userViewModel.sendUserMainIntent(UserMainIntent.Logout)
        }) {
            Text(text = "退出登录")
        }
    }
}