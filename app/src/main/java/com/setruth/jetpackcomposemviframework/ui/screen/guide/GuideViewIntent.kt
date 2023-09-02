package com.setruth.jetpackcomposemviframework.ui.screen.guide

import androidx.lifecycle.ViewModel
import com.setruth.jetpackcomposemviframework.config.KVKey
import com.setruth.jetpackcomposemviframework.config.kv
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

sealed class GuideMainIntent {
    object EnterAPP : GuideMainIntent()
}

@HiltViewModel
class GuideViewModel @Inject constructor():ViewModel() {
    private var _enterAppState = MutableStateFlow(false)
    val enterAppState = _enterAppState.asStateFlow()
    fun sendGuideMainIntent(guideMainIntent: GuideMainIntent) {
        when (guideMainIntent) {
            GuideMainIntent.EnterAPP -> _enterAppState.update {
                kv.putBoolean(KVKey.FIRST_ENTER,true)
                true
            }
        }
    }
}