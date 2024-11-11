package com.omasyo.gatherspace.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun LoginRoute(
    modifier: Modifier = Modifier,
    onSignupTap: () -> Unit,
    onAuthenticated: () -> Unit,
    viewModel: LoginViewModel = viewModel { LoginViewModel(authRepository)}
) {
    val state = viewModel.state.collectAsState().value
    LaunchedEffect(state) {
        if(state is AuthState.Success) {
            onAuthenticated()
        }
    }

    LoginScreen(
        modifier = modifier,
        onSignupTap = onSignupTap,
        username = viewModel.username,
        onUsernameChange = viewModel::changeUsername,
        password = viewModel.password,
        onPasswordChange = viewModel::changePassword,
        onSubmit = viewModel::submit
    )
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    username: String,
    onUsernameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onSignupTap: () -> Unit
) {
    Column(modifier) {
        TextField(value = username, onValueChange = onUsernameChange)
        TextField(value = password, onValueChange = onPasswordChange)
        Button(onClick = onSubmit) {
            Text(text = "Log in")
        }
        Button(onClick = onSignupTap) {
            Text(text = "Swap")
        }
    }
}