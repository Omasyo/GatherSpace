package com.omasyo.gatherspace.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omasyo.gatherspace.dependencyProvider
import com.omasyo.gatherspace.ui.components.TextField
import com.omasyo.gatherspace.ui.components.TextFieldState

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
fun SignupScreen(
    modifier: Modifier = Modifier,
    username: TextFieldState,
    onUsernameChange: (String) -> Unit,
    password: TextFieldState,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onLoginTap: () -> Unit,
    onAuthenticated: () -> Unit,
    state: AuthState
) {
    Column(modifier) {
        TextField(
            value = username.value,
            onValueChange = onUsernameChange,
            supportingText = username.errorMessage,
            isError = username.isError
        )
        TextField(
            value = password.value,
            onValueChange = onPasswordChange,
            supportingText = password.errorMessage,
            isError = password.isError
        )
        Button(onClick = onSubmit) {
            Text(text = "Sign up")
        }
        Button(onClick = onLoginTap) {
            Text(text = "Swap")
        }
    }


    LaunchedEffect(state) {
        if (state is AuthState.Success) {
            onAuthenticated()
        }
        when (state) {
            AuthState.Success -> onAuthenticated()
            is AuthState.Error -> {
                //TODO Indicate error
            }

            else -> Unit
        }
    }
}