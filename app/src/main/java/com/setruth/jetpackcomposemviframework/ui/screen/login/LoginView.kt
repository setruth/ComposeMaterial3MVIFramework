package com.setruth.jetpackcomposemviframework.ui.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.rounded.Mail
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import com.setruth.jetpackcomposemviframework.R
import com.setruth.jetpackcomposemviframework.constant.APPRoute
import com.setruth.jetpackcomposemviframework.ui.components.SingleInput
import com.setruth.jetpackcomposemviframework.ui.theme.APPTheme

@Composable
fun LoginView(
    appNavController: NavHostController,
    loginViewModel: LoginViewModel = hiltViewModel()
) {

    val loginPass by loginViewModel.loginPassState.collectAsState()
    LaunchedEffect(loginPass){
        if (loginPass) {
            appNavController!!.navigate(
                APPRoute.MAIN_VIEW,
                NavOptions.Builder().setPopUpTo(APPRoute.LOGIN, true).build()
            )
        }
    }
    View(loginViewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun View(loginViewModel: LoginViewModel) {
    val loginInfoState by loginViewModel.loginInfoState.collectAsState()
    val loginModeState by loginViewModel.loginModeState.collectAsState()
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                title = { Text(text = "登录") }
            )
        },
    ) {
        val padding = it
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier.size(100.dp),
                painter = painterResource(id = R.mipmap.logo),
                contentDescription = "logo占位"
            )
            Text(
                text = "智能矿山管理系统",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 10.dp, bottom = 30.dp),
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                elevation = CardDefaults.elevatedCardElevation(1.dp)
            ) {
                Column(Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
                    SingleInput(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        inputContent = loginInfoState.loginAct,
                        inputTip = "输入登录账号",
                        startIcon = Icons.Outlined.AccountBox
                    ) {
                        loginViewModel.sendLoginInfoUpdateIntent(LoginInputChangeIntent.Act(it))
                    }
                    Spacer(modifier = Modifier.padding(2.dp))
                    SingleInput(
                        modifier = Modifier.fillMaxWidth(),
                        inputContent = loginInfoState.loginPwd,
                        inputTip = "输入密码账号",
                        startIcon = Icons.Outlined.Lock,
                        activeContentHideTag = true,
                    ) {
                        loginViewModel.sendLoginInfoUpdateIntent(LoginInputChangeIntent.Pwd(it))
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            RadioButton(
                                selected = loginModeState.autoLogin,
                                onClick = {

                                }
                            )
                            Text(
                                text = "自动登录",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            RadioButton(
                                selected = loginModeState.rememberPwd,
                                onClick = { }
                            )
                            Text(
                                text = "记住密码",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                }
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                onClick = { loginViewModel.sendLoginIntent(LoginIntent.LoginRequest) },
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "登录",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                RadioButton(
                    modifier = Modifier.padding(0.dp),
                    selected = false,
                    onClick = { }
                )
                Text(
                    text = "授权即同意服务协议和隐私保护指引",
                    maxLines = 1,
                    style = MaterialTheme.typography.labelSmall,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            Spacer(modifier = Modifier.padding(top = 20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TextButton(
                    modifier = Modifier.padding(top = 15.dp),
                    onClick = {}
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        ),
                        onClick = {

                        }
                    ) {
                        Icon(
                            modifier = Modifier
                                .padding(15.dp)
                                .size(30.dp),
                            imageVector = Icons.Rounded.Public,
                            contentDescription = "网站",
                        )
                    }
                }
                TextButton(
                    modifier = Modifier.padding(top = 15.dp),
                    onClick = {}
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        ),
                        onClick = {

                        }
                    ) {
                        Icon(
                            modifier = Modifier
                                .padding(15.dp)
                                .size(30.dp),
                            imageVector = Icons.Rounded.Mail,
                            contentDescription = "邮箱",
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.padding(top= 15.dp))
            Text(text = "技术支持：上海xxxxxxxxx有限公司", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.padding(vertical = 3.dp))
            Text(text = "软件所属：上海xxxxxxxxx有限公司", style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
@Preview
fun PreviewLoginView() {
    APPTheme {
        View(LoginViewModel())
    }
}