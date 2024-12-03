package com.omasyo.gatherspace.pages.signup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.omasyo.gatherspace.components.layouts.FormLayout


@Composable
fun SignupPage(
    username: TextFieldState,
    onUsernameChange: (String) -> Unit,
    password: TextFieldState,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onLoginTap: () -> Unit,
    onAuthenticated: () -> Unit,
    onBackTap: () -> Unit,
//    state: AuthState,
//    onEventReceived: (AuthEvent) -> Unit,
) {
    FormLayout("Register") {

    }
}