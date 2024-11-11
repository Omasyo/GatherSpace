package com.omasyo.gatherspace.domain.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.omasyo.gatherspace.models.TokenStorage
import com.omasyo.gatherspace.models.response.TokenResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")

class AndroidTokenStorage(
    private val dataStore: DataStore<Preferences>
) : TokenStorage {
    private val tokenKey = stringPreferencesKey("token")


    override suspend fun observeToken(): Flow<TokenResponse?> {
        return dataStore.data.map { preferences ->
            preferences[tokenKey]?.let {
                Json.decodeFromString<TokenResponse>(it)
            }
        }
    }

    override suspend fun getToken(): TokenResponse? {
        return observeToken().first()
    }

    override suspend fun saveToken(tokenResponse: TokenResponse) {
        dataStore.edit { preferences ->
            preferences[tokenKey] = Json.encodeToString(tokenResponse)
        }
    }

    override suspend fun clearToken() {
        dataStore.edit { preferences ->
            preferences.remove(tokenKey)
        }
    }
}