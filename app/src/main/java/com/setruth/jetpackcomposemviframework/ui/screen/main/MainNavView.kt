package com.setruth.jetpackcomposemviframework.ui.screen.main

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.setruth.jetpackcomposemviframework.constant.MainNavRoute
import com.setruth.jetpackcomposemviframework.ui.screen.main.home.HomeView
import com.setruth.jetpackcomposemviframework.ui.screen.main.user.UserView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(appNavController: NavHostController) {
    val mainViewNavController = rememberNavController()
    val mainNavItemList =
        listOf(
            Triple("home", "主页", Icons.Outlined.Home),
            Triple("user", "个人", Icons.Outlined.AccountBox)
        )
    var bottomNavSelect by remember { mutableStateOf(0) }
    val snackBarState = remember { SnackbarHostState() }
    NavDestination
    mainViewNavController.addOnDestinationChangedListener { _, destination, _ ->
        for ((index, item) in mainNavItemList.withIndex()) {
            Log.e("TAG", "MainView:${destination.route},${index} ${item.first}")
            if (item.first == destination.route) {
                Log.e("TAG", "MainView:${destination.route},${index} ${item.first}")
                bottomNavSelect = index
                break;
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = mainNavItemList[bottomNavSelect].second,
                    style = MaterialTheme.typography.titleMedium
                )
            })
        },
        bottomBar = {
            BottomNav(bottomNavSelect, mainNavItemList) {
                when (it) {
                    0 -> mainViewNavController.navigateSingleTopTo(MainNavRoute.Home)
                    1 -> mainViewNavController.navigateSingleTopTo(MainNavRoute.User)
                }
            }
        },
        snackbarHost = {
            SnackbarHost(snackBarState) {
                Snackbar(
                    modifier = Modifier.padding(15.dp),
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Text(it.visuals.message, color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            NavHost(navController = mainViewNavController, startDestination = MainNavRoute.Home) {
                composable(MainNavRoute.Home) {
                    HomeView(snackBarState, appNavController)
                }
                composable(MainNavRoute.User) {
                    UserView(snackBarState, appNavController)
                }
            }
        }
    }
}

@Composable
fun BottomNav(
    nowIndex: Int,
    itemList: List<Triple<String, String, ImageVector>>,
    click: (index: Int) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        itemList.forEachIndexed { index, pair ->
            NavigationBarItem(
                icon = {
                    Row {
                        Icon(
                            pair.third,
                            contentDescription = pair.first,
                        )
                    }
                },
                label = { Text(pair.second) },
                selected = (nowIndex == index),
                onClick = { click(index) },
                colors = NavigationBarItemDefaults.colors(
                    unselectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
            )
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }