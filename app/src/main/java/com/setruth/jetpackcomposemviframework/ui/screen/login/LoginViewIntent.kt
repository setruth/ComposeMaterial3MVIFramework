package com.setruth.jetpackcomposemviframework.ui.screen.login

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.setruth.jetpackcomposemviframework.constant.PDSKey
import com.setruth.jetpackcomposemviframework.model.state.TipShowState
import com.setruth.jetpackcomposemviframework.network.RequestBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
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
    private val requestBuilder: RequestBuilder,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            initLoginConfig()
        }
    }
    //登录通过状态
    private var _loginRequestState = MutableStateFlow(LoginRequestState.NOTING)
    val loginRequestState = _loginRequestState.asStateFlow()

    //登录的信息
    private var _loginInfoState = MutableStateFlow(LoginInfoState())
    val loginInfoState = _loginInfoState.asStateFlow()

    //登录的模式(记住密码和自动登录)
    private var _loginModeState = MutableStateFlow(LoginModeState())
    val loginModeState = _loginModeState.asStateFlow()

    //提示显示状态
    private var _tipShowState = MutableStateFlow(TipShowState())
    val tipShowState = _tipShowState.asStateFlow()
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

    private fun showTip(tipMsg: String) = viewModelScope.launch {
        _tipShowState.update {
            it.copy(showTip = true, tipMsg = tipMsg)
        }
        delay(1500)
        _tipShowState.update {
            it.copy(showTip = false, tipMsg = "")
        }
    }

    private fun loginRequest() = viewModelScope.launch {
        loginInfoState.value.apply {
            if (loginAct == ""){
                showTip("账号不能为空")
                return@launch
            }
            if (loginPwd == ""){
                showTip("密码不能为空")
                return@launch
            }
        }
        _loginRequestState.update {
            LoginRequestState.LOADING
        }
        //模拟网络请求
        delay(2000L)
        val preferences = dataStore.data.first()
        dataStore.edit {
            it[PDSKey.ACCOUNT_PDS] = loginInfoState.value.loginAct
            it[PDSKey.PASSWORD_PDS] = loginInfoState.value.loginPwd
        }
        _loginRequestState.update {
            LoginRequestState.SUCCESS
        }

        //网络工具使用示例
//        requestBuilder.apply {
//            getResponse {
//                getAPI(UserAPI::class.java).login(
//                    LoginBody(
//                        account = loginInfoState.value.loginAct,
//                        password = loginInfoState.value.loginPwd
//                    )
//                ).execute()
//            }.collect {
//                when (it) {
//                    is RequestStatus.Error -> {
//                        Log.e("TAG", "loginRequest:${it.errMsg} ${it.exception} ", )
//                    }
//                    RequestStatus.Loading -> {}
//                    is RequestStatus.Success -> {
//                        _loginRequestState.update {
//                            LoginRequestState.SUCCESS
//                        }
//                    }
//                }
//            }
//        }
    }
    private suspend fun initLoginConfig(){
        val preferences = dataStore.data.first()
        preferences[PDSKey.ACCOUNT_PDS]?.also { loginAccount->
            _loginInfoState.update {
                it.copy(loginAct = loginAccount)
            }
        }
    }
}
