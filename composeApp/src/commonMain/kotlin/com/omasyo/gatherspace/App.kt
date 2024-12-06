package com.omasyo.gatherspace

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omasyo.gatherspace.domain.deps.DomainComponent
import com.omasyo.gatherspace.ui.theme.GatherSpaceTheme
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(
    appViewModel: AppViewModel = dependencyProvider { viewModel{ AppViewModel(authRepository) } }
) {
    LaunchedEffect(Unit) {
        Napier.base(DebugAntilog())
        Napier.i { "Starting Napier" }
    }
    GatherSpaceTheme {
        AppNavHost()
    }
}

val DomainComponent = staticCompositionLocalOf<DomainComponent?> { null }

@Composable
inline fun <T> dependencyProvider(provider: @Composable DomainComponent.() -> T): T {
    return DomainComponent.current?.let { provider(it) }
        ?: throw IllegalStateException("DomainComponent not set")
}