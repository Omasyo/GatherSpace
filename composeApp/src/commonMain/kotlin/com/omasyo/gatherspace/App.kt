package com.omasyo.gatherspace

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omasyo.gatherspace.domain.deps.DomainComponent
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(
    appViewModel: AppViewModel = dependencyProvider { AppViewModel(authRepository) }
) {
    MaterialTheme {
        AppNavHost(
            isAuthenticated = appViewModel.isAuthenticated.collectAsStateWithLifecycle().value,
            logout = appViewModel::logout,
        )
    }
}

val DomainComponent = staticCompositionLocalOf<DomainComponent?> { null }

@Composable
inline fun <T> dependencyProvider(provider: @Composable DomainComponent.() -> T): T {
    return DomainComponent.current?.let { provider(it) }
        ?: throw IllegalStateException("DomainComponent not set")
}