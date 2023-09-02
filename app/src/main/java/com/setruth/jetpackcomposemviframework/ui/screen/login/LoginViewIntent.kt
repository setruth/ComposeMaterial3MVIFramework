package com.setruth.jetpackcomposemviframework.ui.screen.login

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.setruth.jetpackcomposemviframework.config.KVKey
import com.setruth.jetpackcomposemviframework.config.PDSKey
import com.setruth.jetpackcomposemviframework.config.kv
import com.setruth.jetpackcomposemviframework.model.body.LoginBody
import com.setruth.jetpackcomposemviframework.model.state.TipShowState
import com.setruth.jetpackcomposemviframework.network.RequestBuilder
import com.setruth.jetpackcomposemviframework.network.RequestStatus
import com.setruth.jetpackcomposemviframework.network.api.UserAPI
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

sealed class LoginModeIntent {
    object ChangeRememberPwdMode : LoginModeIntent()
    object ChangeAutoLoginMode : LoginModeIntent()
    object ChangeAgreementMode : LoginModeIntent()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val requestBuilder: RequestBuilder,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            initLoginAllState()
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

    /*****************************handler intent*****************************************/
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

    fun sendLoginModeUpdateIntent(loginModeIntent: LoginModeIntent) {
        when (loginModeIntent) {
            LoginModeIntent.ChangeAutoLoginMode -> {
                _loginModeState.update {
                    it.copy(autoLogin = !it.autoLogin)
                }
                if (!loginModeState.value.rememberPwd) {
                    _loginModeState.update {
                        it.copy(rememberPwd = !it.rememberPwd)
                    }
                }
            }

            LoginModeIntent.ChangeRememberPwdMode -> {
                _loginModeState.update {
                    it.copy(rememberPwd = !it.rememberPwd)
                }
                if (!loginModeState.value.rememberPwd && loginModeState.value.autoLogin) {
                    _loginModeState.update {
                        it.copy(autoLogin = !it.autoLogin)
                    }
                }
            }

            LoginModeIntent.ChangeAgreementMode -> {
                _loginModeState.update {
                    it.copy(agreement = !it.agreement)
                }
            }
        }
    }

    /*****************************private fun*****************************************/

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
            if (loginAct == "") {
                showTip("账号不能为空")
                return@launch
            }
            if (loginPwd == "") {
                showTip("密码不能为空")
                return@launch
            }
        }
        if (!loginModeState.value.agreement) {
            showTip("请同意用户协议")
            return@launch
        }
        _loginRequestState.update {
            LoginRequestState.LOADING
        }
        //模拟网络请求
        delay(2000L)
        saveLoginAllState()
        _loginRequestState.update {
            LoginRequestState.SUCCESS
        }

        //网络工具使用示例
//        requestBuilder.apply {
//            getResponse {
//                getAPI(UserAPI::class).login(
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

    private suspend fun initLoginAllState() {
        val preferences = dataStore.data.first()
        preferences[PDSKey.ACCOUNT_PDS]?.also { loginAccount ->
            _loginInfoState.update {
                it.copy(loginAct = loginAccount)
            }
        }
        _loginModeState.update {
            it.copy(agreement = true)
        }
        if (!kv.getBoolean(KVKey.REMEMBER_PWD, false)) return
        preferences[PDSKey.PASSWORD_PDS]?.also { loginPassword ->
            _loginInfoState.update {
                it.copy(loginPwd = loginPassword)
            }
        }
        _loginModeState.update {
            it.copy(rememberPwd = true, autoLogin = kv.getBoolean(KVKey.AUTO_LOGIN,false))
        }

        if (loginModeState.value.autoLogin) {
            loginRequest()
        }
    }

    private suspend fun saveLoginAllState() {
        saveLoginInfo()
        saveLoginMode()
    }

    private fun saveLoginMode() = loginModeState.value.apply {
        kv.putBoolean(KVKey.AUTO_LOGIN, autoLogin)
        kv.putBoolean(KVKey.REMEMBER_PWD, rememberPwd)
        kv.putBoolean(KVKey.AGREEMENT, agreement)
    }

    private suspend fun saveLoginInfo() {
        dataStore.edit {
            it[PDSKey.ACCOUNT_PDS] = loginInfoState.value.loginAct
            it[PDSKey.PASSWORD_PDS] = loginInfoState.value.loginPwd
        }
    }
}
