package com.setruth.jetpackcomposemviframework.ui.screen.login

data class LoginInfoState(
    var loginAct:String="",
    var loginPwd:String=""

)
data class LoginModeState(
    var autoLogin:Boolean=false,
    var rememberPwd:Boolean=false,
    var agreement:Boolean=false
)
enum class LoginRequestState{
    LOADING,
    ERR,
    SUCCESS,
    NOTING
}