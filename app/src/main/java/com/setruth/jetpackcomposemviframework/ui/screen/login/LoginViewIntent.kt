package com.setruth.jetpackcomposemviframework.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.setruth.jetpackcomposemviframework.model.body.LoginBody
import com.setruth.jetpackcomposemviframework.network.RequestBuilder
import com.setruth.jetpackcomposemviframework.network.RequestStatus
import com.setruth.jetpackcomposemviframework.network.api.UserAPI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LoginIntent {
    object LoginRequest : LoginIntent()
}

sealed class LoginInputChangeIntent {
    data class Pwd(val newPwd: String) : LoginInputChangeIntent()
    data class Act(val newAct: String) : LoginInputChangeIntent()

}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val requestBuilder: RequestBuilder
) : ViewModel() {
    //登录通过状态
    private var _loginPassState = MutableStateFlow(false)
    val loginPassState = _loginPassState.asStateFlow()

    //登录的信息
    private var _loginInfoState = MutableStateFlow(LoginInfoState())
    val loginInfoState = _loginInfoState.asStateFlow()

    //登录的模式(记住密码和自动登录)
    private var _loginModeState = MutableStateFlow(LoginModeState())
    val loginModeState = _loginModeState.asStateFlow()
    fun sendLoginIntent(intent: LoginIntent) {
        when (intent) {
            LoginIntent.LoginRequest -> loginRequest()
        }
    }

    fun sendLoginInfoUpdateIntent(intent: LoginInputChangeIntent) {
        when (intent) {
            is LoginInputChangeIntent.Act -> _loginInfoState.update {
                it.copy(loginAct = intent.newAct)
            }

            is LoginInputChangeIntent.Pwd -> _loginInfoState.update {
                it.copy(loginPwd = intent.newPwd)
            }
        }
    }

    private fun loginRequest() {
        requestBuilder.apply {
            viewModelScope.launch {
                getResponse {
                    getAPI(UserAPI::class.java).login(
                        LoginBody(
                            account = loginInfoState.value.loginAct,
                            password = loginInfoState.value.loginPwd
                        )
                    ).execute()
                }.collect{
                    when (it) {
                        is RequestStatus.Error -> {}
                        RequestStatus.Loading -> {}
                        is RequestStatus.Success -> {
                            _loginPassState.value = true
                        }
                    }
                }
            }
        }

    }
}