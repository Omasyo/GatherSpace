package com.omasyo.gatherspace.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.omasyo.gatherspace.domain.auth.AuthRepository
import com.omasyo.gatherspace.domain.user.UserRepository
import com.omasyo.gatherspace.models.TokenStorage
import com.omasyo.gatherspace.models.response.TokenResponse
import com.omasyo.gatherspace.network.auth.AuthNetworkSource
import com.omasyo.gatherspace.network.createClient
import com.omasyo.gatherspace.network.user.UserNetworkSource
import kotlinx.coroutines.Dispatchers

object Storage : TokenStorage {
    var tokenResponse: TokenResponse? = null

    override suspend fun getToken(): TokenResponse? {
        return tokenResponse
    }

    override suspend fun saveToken(tokenResponse: TokenResponse) {
        this.tokenResponse = tokenResponse
    }

    override suspend fun clearToken() {
        tokenResponse = null
    }

}

val client = createClient(tokenStorage = Storage)
val userNetworkSource = UserNetworkSource(client)
val userRepository = UserRepository(userNetworkSource, Dispatchers.IO)
val authNetworkSource = AuthNetworkSource(client)
val authRepository = AuthRepository(authNetworkSource, Storage, Dispatchers.IO)


@Composable
fun SignupRoute(
    modifier: Modifier = Modifier,
    onLoginTap: () -> Unit,
    onAuthenticated: () -> Unit,
    viewModel: SignupViewModel = androidx.lifecycle.viewmodel.compose.viewModel {
        SignupViewModel(
            authRepository,
            userRepository
        )
    }
) {
    val state = viewModel.state.collectAsState().value
    LaunchedEffect(state) {
        if (state is AuthState.Success) {
            onAuthenticated()
        }
    }

    SignupScreen(
        modifier = modifier,
        onLoginTap = onLoginTap,
        username = viewModel.username,
        onUsernameChange = viewModel::changeUsername,
        password = viewModel.password,
        onPasswordChange = viewModel::changePassword,
        onSubmit = viewModel::submit
    )
}

@Composable
fun SignupScreen(
    modifier: Modifier = Modifier,
    username: String,
    onUsernameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onLoginTap: () -> Unit
) {
    Column(modifier) {
        TextField(value = username, onValueChange = onUsernameChange)
        TextField(value = password, onValueChange = onPasswordChange)
        Button(onClick = onSubmit) {
            Text(text = "Sign up")
        }
        Button(onClick = onLoginTap) {
            Text(text = "Swap")
        }
    }
}