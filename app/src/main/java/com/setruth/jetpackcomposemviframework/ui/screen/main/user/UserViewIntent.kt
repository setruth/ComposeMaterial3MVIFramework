package com.setruth.jetpackcomposemviframework.ui.screen.main.user

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.setruth.jetpackcomposemviframework.constant.KVKey
import com.setruth.jetpackcomposemviframework.constant.PDSKey
import com.setruth.jetpackcomposemviframework.constant.kv
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UserMainIntent{
    object Logout:UserMainIntent()
}
@HiltViewModel
class UserViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>
):ViewModel(){
    private var _logoutState = MutableStateFlow(false)
    val logoutState = _logoutState.asStateFlow()

    fun sendUserMainIntent(userMainIntent: UserMainIntent){
        when (userMainIntent) {
            UserMainIntent.Logout ->logout()
        }
    }
    private fun logout()=viewModelScope.launch{
        kv.putBoolean(KVKey.AUTO_LOGIN,false)
        kv.putBoolean(KVKey.REMEMBER_PWD,false)
        dataStore.edit {
            it[PDSKey.PASSWORD_PDS]=""
        }
        _logoutState.update {
            true
        }
    }
}