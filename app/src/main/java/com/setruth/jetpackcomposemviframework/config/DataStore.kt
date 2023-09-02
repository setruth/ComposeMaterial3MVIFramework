package com.setruth.jetpackcomposemviframework.config

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PDSKey.PDS_NAME)
object PDSKey{
    const val PDS_NAME="pds"
    val PASSWORD_PDS = stringPreferencesKey("password")
    val ACCOUNT_PDS = stringPreferencesKey("account")
}