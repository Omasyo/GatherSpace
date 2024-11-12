package com.omasyo.gatherspace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import com.omasyo.gatherspace.domain.auth.AndroidTokenStorage
import com.omasyo.gatherspace.domain.auth.dataStore
import com.omasyo.gatherspace.domain.deps.DomainComponent
import com.omasyo.gatherspace.models.TokenStorage
import com.omasyo.gatherspace.network.deps.NetworkComponent
import kotlinx.coroutines.Dispatchers

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CompositionLocalProvider(DomainComponent provides domainComponent) {
                App()
            }
        }
    }

    private val tokenStorage: TokenStorage by lazy { AndroidTokenStorage(dataStore) }

    private val networkComponent by lazy { NetworkComponent(tokenStorage) }

    private val domainComponent by lazy {
        DomainComponent(
            networkComponent = networkComponent,
            tokenStorage = tokenStorage,
            dispatcher = Dispatchers.IO
        )
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}