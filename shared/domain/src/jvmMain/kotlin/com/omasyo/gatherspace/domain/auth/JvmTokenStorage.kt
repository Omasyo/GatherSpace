package com.omasyo.gatherspace.domain.auth

import com.omasyo.gatherspace.models.TokenStorage
import com.omasyo.gatherspace.models.response.TokenResponse
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.prefs.PreferenceChangeEvent
import java.util.prefs.Preferences

class JvmTokenStorage(private val preferences: Preferences) : TokenStorage {
    private val tokenKey = "token"

    override fun observeToken(): Flow<TokenResponse?> {
        return callbackFlow {
            val listener = { event: PreferenceChangeEvent ->
                if (event.key == tokenKey) {
                    trySend(event.newValue?.let { Json.decodeFromString<TokenResponse>(it) })
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