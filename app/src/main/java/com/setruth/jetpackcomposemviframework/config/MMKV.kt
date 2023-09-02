package com.setruth.jetpackcomposemviframework.config

import com.tencent.mmkv.MMKV
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

val kv: MMKV by MyMMKV()

class MyMMKV:ReadWriteProperty<Nothing?,MMKV>{
    val mmkv=MMKV.defaultMMKV()
    override fun getValue(thisRef: Nothing?, property: KProperty<*>): MMKV =mmkv

    override fun setValue(thisRef: Nothing?, property: KProperty<*>, value: MMKV) {

    }

}
object KVKey{
    const val FIRST_ENTER="first_enter"
    const val AUTO_LOGIN="auto_login"
    const val REMEMBER_PWD="remember_pwd"
    const val AGREEMENT="agreement"
    const val TOKEN="token"
}

