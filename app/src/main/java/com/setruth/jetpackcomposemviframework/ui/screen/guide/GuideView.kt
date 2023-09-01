package com.setruth.jetpackcomposemviframework.ui.screen.guide

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.setruth.jetpackcomposemviframework.config.APPRoute

@Composable
fun GuideView(
    appNavController: NavController,
    guideViewModel: GuideViewModel = hiltViewModel()
) {
    val enterAppState by guideViewModel.enterAppState.collectAsState()
    LaunchedEffect(enterAppState) {
        if (enterAppState) {
            appNavController.navigate(
                APPRoute.LOGIN_VIEW,
                NavOptions.Builder().setPopUpTo(APPRoute.GUIDE_VIEW, true).build()
            )
        }
    }
    Text(text = "引导页")
    Button(onClick = {
        guideViewModel.sendGuideMainIntent(GuideMainIntent.EnterAPP)
    }) {
        Text(text = "进入APP")
    }
}