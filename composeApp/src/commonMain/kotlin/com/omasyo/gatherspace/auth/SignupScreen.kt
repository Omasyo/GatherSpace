package com.omasyo.gatherspace.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omasyo.gatherspace.TextFieldState
import com.omasyo.gatherspace.dependencyProvider

@Composable
fun SignupRoute(
    modifier: Modifier = Modifier,
    onLoginTap: () -> Unit,
    onBackTap: () -> Unit,
    onAuthenticated: () -> Unit,
    viewModel: SignupViewModel = dependencyProvider {
        viewModel {
            SignupViewModel(
                authRepository,
                userRepository
            )
        }
    }
) {
    SignupScreen(
        modifier = modifier,
        onLoginTap = onLoginTap,
        username = viewModel.usernameField,
        onUsernameChange = viewModel::changeUsername,
        password = viewModel.passwordField,
        onPasswordChange = viewModel::changePassword,
        onSubmit = viewModel::submit,
        onAuthenticated = onAuthenticated,
        onBackTap = onBackTap,
        state = viewModel.state.collectAsStateWithLifecycle().value,
        onEventReceived = { viewModel.clearEvent() }
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
    onBackTap: () -> Unit,
    state: AuthState,
    onEventReceived: (AuthEvent) -> Unit,
) {
    AuthScreen(
        modifier = modifier,
        onAuthenticated = onAuthenticated,
        onBackTap = onBackTap,
        state = state,
        onEventReceived = onEventReceived,
    ) {
        Column(
            Modifier
                .widthIn(max = 320f.dp)
                .padding(16f.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End,
        ) {
            Text(
                "Create an account",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(24f.dp))
            AuthTextField(
                state = username,
                onValueChange = onUsernameChange,
                hint = "Username",
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                )
            )
            Spacer(Modifier.height(8.dp))
            AuthTextField(
                state = password,
                onValueChange = onPasswordChange,
                hint = "Password",
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                )
            )
            Spacer(Modifier.height(16.dp))
            Button(onClick = onSubmit) {
                Text(text = "Signup")
            }
            Spacer(Modifier.height(4.dp))
            FilledTonalButton(onClick = onLoginTap) {
                Text(text = "Login")
            }
        }
    }
}