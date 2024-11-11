package com.omasyo.gatherspace

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

val t = flow {
    emit(false)
}

@Composable
fun isAuthenticated(): Boolean {
    return t.collectAsStateWithLifecycle(false).value
}