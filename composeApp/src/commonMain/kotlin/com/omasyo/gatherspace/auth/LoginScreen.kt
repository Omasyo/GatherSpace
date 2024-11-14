package com.omasyo.gatherspace.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omasyo.gatherspace.dependencyProvider
import com.omasyo.gatherspace.ui.components.TextField
import com.omasyo.gatherspace.ui.components.TextFieldState


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
            Text(text = "Log in")
        }
        Button(onClick = onSignupTap) {
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