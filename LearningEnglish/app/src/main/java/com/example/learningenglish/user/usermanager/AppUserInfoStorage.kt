package com.example.learningenglish.user.usermanager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.learningenglish.utils.JsonParse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import timber.log.Timber


private val Context.appUserInfoStore: DataStore<Preferences> by preferencesDataStore("user_info")

class AppUserInfoStorage(context: Context) : IUserInfoStorage<AppUserInfo> {
    private val dataStore: DataStore<Preferences> = context.appUserInfoStore
    private val KEY = stringPreferencesKey("user_info")

    override suspend fun saveUserInfo(userInfo: AppUserInfo) {
        dataStore.edit {
            it[KEY] = JsonParse.toJson(userInfo)
        }
    }

    override fun getUserInfo(): Flow<AppUserInfo?> {
        return dataStore.data.map {
            val s = it[KEY]
            Timber.d(" data store info : $s")
            try {
                JsonParse.fromJson(s, AppUserInfo::class.java)
            } catch (e: Exception) {
                Timber.e(" parse json error : ${e.message}")
                e.printStackTrace()
                null
            }
        }
    }

    override suspend fun deleteUserInfo(): Boolean {
        dataStore.edit { it.remove(KEY) }
        return true
    }

    override fun checkUserInfoLogin(): Flow<Boolean> {
        return dataStore.data.map {
            val userInfo = getUserInfo()
            it.contains(KEY) && userInfo.first()?.id != null
        }
    }

}