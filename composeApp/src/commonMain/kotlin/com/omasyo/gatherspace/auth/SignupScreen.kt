package com.omasyo.gatherspace.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.omasyo.gatherspace.dependencyProvider

@Composable
fun SignupRoute(
    modifier: Modifier = Modifier,
    onLoginTap: () -> Unit,
    onAuthenticated: () -> Unit,
    viewModel: SignupViewModel = dependencyProvider {
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