package com.setruth.jetpackcomposemviframework.ui.screen.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
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

    val loginRequestState by loginViewModel.loginRequestState.collectAsState()
    LaunchedEffect(loginRequestState) {
        if (loginRequestState==LoginRequestState.SUCCESS){
            appNavController.navigate(
                APPRoute.MAIN_VIEW,
                NavOptions.Builder().setPopUpTo(APPRoute.LOGIN, true).build()
            )
        }
    }
    View(loginViewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun View(loginViewModel: LoginViewModel = hiltViewModel()) {
    val loginRequestState by loginViewModel.loginRequestState.collectAsState()
    val loginInfoState by loginViewModel.loginInfoState.collectAsState()
    val loginModeState by loginViewModel.loginModeState.collectAsState()
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxSize(),
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
                modifier = Modifier.size(90.dp),
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

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                onClick = {
                    loginViewModel.sendLoginIntent(LoginIntent.LoginRequest)
                },
                enabled = loginRequestState!=LoginRequestState.LOADING,
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AnimatedVisibility(
                        visible = loginRequestState==LoginRequestState.LOADING,
                        enter = fadeIn()+expandVertically(),
                        exit = fadeOut()+shrinkVertically(

                        )
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    AnimatedVisibility(
                        visible = loginRequestState!=LoginRequestState.LOADING,
                        enter = fadeIn()+expandVertically(expandFrom =Alignment.Top ),
                        exit = fadeOut()+shrinkVertically(shrinkTowards = Alignment.Top)
                    ) {
                        Text(
                            text = "登录",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }


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
           Row(modifier = Modifier.padding(top = 20.dp), verticalAlignment = Alignment.CenterVertically) {
               Divider(modifier = Modifier.weight(1f))
               Text(text = "联系我们", modifier = Modifier.padding(horizontal = 10.dp), style = MaterialTheme.typography.labelSmall)
               Divider(modifier = Modifier.weight(1f))
           }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TextButton(
                    onClick = {}
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(10.dp)
                            .size(30.dp),
                        imageVector = Icons.Rounded.Public,
                        contentDescription = "网站",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
                TextButton(
                    onClick = {}
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(10.dp)
                            .size(30.dp),
                        tint = MaterialTheme.colorScheme.secondary,
                        imageVector = Icons.Rounded.Mail,
                        contentDescription = "邮箱",
                    )
                }
            }
            Spacer(modifier = Modifier.padding(top = 15.dp))
            Text(
                text = "技术支持：上海xxxxxxxxx有限公司",
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(modifier = Modifier.padding(vertical = 3.dp))
            Text(
                text = "软件所属：上海xxxxxxxxx有限公司",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
@Preview
fun PreviewLoginView() {
    APPTheme {
        View()
    }
}