package com.setruth.jetpackcomposemviframework.constant

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
}

