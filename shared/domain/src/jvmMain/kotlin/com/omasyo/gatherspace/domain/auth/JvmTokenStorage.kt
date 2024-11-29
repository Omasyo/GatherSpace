package com.omasyo.gatherspace.domain.auth

import com.omasyo.gatherspace.TokenStorage
import com.omasyo.gatherspace.models.response.TokenResponse
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.prefs.PreferenceChangeListener
import java.util.prefs.Preferences

class JvmTokenStorage(private val preferences: Preferences) : TokenStorage {
    private val tokenKey = "token"

    override fun observeToken(): Flow<TokenResponse?> {
        return callbackFlow {
            val listener = PreferenceChangeListener { evt ->
                if (evt?.key == tokenKey) {
                    trySend(evt.newValue?.let { Json.decodeFromString<TokenResponse>(it) })
                }
            }
            send(getToken())
            preferences.addPreferenceChangeListener(listener)
            awaitClose { preferences.removePreferenceChangeListener(listener) }
        }
    }

    override suspend fun getToken(): TokenResponse? {
        return preferences.get(tokenKey, null)?.let { Json.decodeFromString<TokenResponse>(it) }
    }

    override suspend fun saveToken(tokenResponse: TokenResponse) {
        preferences.put(tokenKey, Json.encodeToString(tokenResponse))
    }

    override suspend fun clearToken() {
        preferences.remove(tokenKey)
    }
}