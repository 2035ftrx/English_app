package com.example.learningenglish.http.token

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.appLoginStore: DataStore<Preferences> by preferencesDataStore(name = "app_login")

class LoginStateStorage(context: Context) : ILoginStateStorage {
    private val dataStore: DataStore<Preferences> = context.appLoginStore
    private val REMEMBER_KEY = booleanPreferencesKey("remember_login")

    override suspend fun isRemember(): Flow<Boolean> {
        return dataStore.data.map { it[REMEMBER_KEY] ?: false }
    }

    override suspend fun setRemember(remember: Boolean) {
        dataStore.edit { tokenStore ->
            tokenStore[REMEMBER_KEY] = remember
        }
    }
}