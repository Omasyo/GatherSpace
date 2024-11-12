package com.omasyo.gatherspace.domain.auth

import com.omasyo.gatherspace.models.TokenStorage

interface TokenStorageFactory {
    fun createTokenStorage(): TokenStorage
}

fun TokenStorage(tokenStorageFactory: TokenStorageFactory): TokenStorage {
    return tokenStorageFactory.createTokenStorage()
}