package com.example.learningenglish.http.token

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

private val Context.appTokenStore: DataStore<Preferences> by preferencesDataStore(name = "app_token")

class AppUserTokenStorage(context: Context) : ITokenStorage {

    private val tokenStore: DataStore<Preferences> = context.appTokenStore
    private val token_key = stringPreferencesKey("user_token")

    override fun saveToken(token: String) {
        runBlocking {
            tokenStore.edit { tokenStore ->
                tokenStore[token_key] = token
            }
        }
    }

    override fun getToken(): String? {
        return runBlocking { tokenStore.data.first()[token_key] }
    }

    override fun cleanToken(): Boolean {
        return runBlocking { tokenStore.edit { it.remove(token_key) };true }
    }
}
