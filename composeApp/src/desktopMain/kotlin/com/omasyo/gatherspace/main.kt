package com.omasyo.gatherspace

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.omasyo.gatherspace.domain.auth.JvmTokenStorage
import com.omasyo.gatherspace.domain.deps.DomainComponent
import com.omasyo.gatherspace.network.deps.NetworkComponent
import kotlinx.coroutines.Dispatchers
import java.lang.invoke.MethodHandles
import java.util.prefs.Preferences

val tokenStorage =
    JvmTokenStorage(Preferences.userNodeForPackage(MethodHandles.lookup().lookupClass()))

val networkComponent = NetworkComponent(tokenStorage)

val domainComponent = DomainComponent(networkComponent, tokenStorage, Dispatchers.IO)

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "GatherSpace",
    ) {
        CompositionLocalProvider(DomainComponent provides domainComponent) {
            App()
        }
    }
}