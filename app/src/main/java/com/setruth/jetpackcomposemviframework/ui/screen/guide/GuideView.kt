package com.setruth.jetpackcomposemviframework.ui.screen.guide

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.setruth.jetpackcomposemviframework.config.APPRoute

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GuideView(
    appNavController: NavController,
    guideViewModel: GuideViewModel = hiltViewModel()
) {
    val enterAppState by guideViewModel.enterAppState.collectAsState()
    val guidePageItemBackground = listOf(
        MaterialTheme.colorScheme.background,
        MaterialTheme.colorScheme.surface,
        MaterialTheme.colorScheme.primaryContainer,
        MaterialTheme.colorScheme.secondaryContainer,
    )
    LaunchedEffect(enterAppState) {
        if (enterAppState) {
            appNavController.navigate(
                APPRoute.LOGIN_VIEW,
                NavOptions.Builder().setPopUpTo(APPRoute.GUIDE_VIEW, true).build()
            )
        }

    }
    HorizontalPager(pageCount = guidePageItemBackground.size) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(guidePageItemBackground[it])
        ) {
            Text(text = "引导页${it + 1}")
            if (it == guidePageItemBackground.size - 1) {
                Button(
                    modifier = Modifier.padding(top = 10.dp),
                    onClick = {
                        guideViewModel.sendGuideMainIntent(GuideMainIntent.EnterAPP)
                    }
                ) {
                    Text(text = "进入APP")
                }
            }
        }
    }

}