package com.omasyo.gatherspace.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omasyo.gatherspace.dependencyProvider
import com.omasyo.gatherspace.ui.components.TextFieldState
import com.omasyo.gatherspace.ui.theme.GatherSpaceTheme
import androidx.compose.desktop.ui.tooling.preview.Preview


@Composable
fun LoginRoute(
    modifier: Modifier = Modifier,
    onSignupTap: () -> Unit,
    onAuthenticated: () -> Unit,
    viewModel: LoginViewModel = dependencyProvider { viewModel { LoginViewModel(authRepository) } }
) {
    LoginScreen(
        modifier = modifier,
        onSignupTap = onSignupTap,
        username = viewModel.usernameField,
        onUsernameChange = viewModel::changeUsername,
        password = viewModel.passwordField,
        onPasswordChange = viewModel::changePassword,
        onSubmit = viewModel::submit,
        onAuthenticated = onAuthenticated,
        state = viewModel.state.collectAsStateWithLifecycle().value,
    )
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    username: TextFieldState,
    onUsernameChange: (String) -> Unit,
    password: TextFieldState,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onSignupTap: () -> Unit,
    onAuthenticated: () -> Unit,
    state: AuthState
) {
    AuthScreen(
        modifier = modifier,
        onAuthenticated = onAuthenticated,
        state = state
    ) {
        Column(
            Modifier
                .widthIn(max = 320f.dp)
                .padding(16f.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End,
        ) {
            Text(
                "Log in to your account",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(24f.dp))
            AuthTextField(
                state = username,
                onValueChange = onUsernameChange,
                hint = "Username",
            )
            Spacer(Modifier.height(8.dp))
            AuthTextField(
                state = password,
                onValueChange = onPasswordChange,
                hint = "Password",
            )
            Spacer(Modifier.height(16.dp))
            Button(onClick = onSubmit) {
                Text(text = "Login")
            }
            Spacer(Modifier.height(4.dp))
            FilledTonalButton(onClick = onSignupTap) {
                Text(text = "Signup")
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    GatherSpaceTheme {
        LoginScreen(
            modifier = Modifier.fillMaxSize(),
            username = TextFieldState("", errorMessage = "Why don't have an account?"),
            onUsernameChange = {},
            password = TextFieldState(""),
            onPasswordChange = {},
            onSubmit = {},
            onSignupTap = {},
            onAuthenticated = {},
            state = AuthState.Idle
        )
    }
}