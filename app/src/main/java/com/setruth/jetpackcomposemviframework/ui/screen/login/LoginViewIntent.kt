package com.setruth.jetpackcomposemviframework.ui.screen.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

sealed class LoginIntent{
    object Login:LoginIntent()
}

@HiltViewModel
class LoginViewModel @Inject constructor():ViewModel(){
    private var _loginPass = MutableStateFlow(false)
    val loginPass=_loginPass.asStateFlow()
    fun handlerLoginIntent(intent: LoginIntent){
        when (intent) {
            LoginIntent.Login -> loginRequest()
        }
    }
    private fun loginRequest(){
        _loginPass.value=true
    }
}