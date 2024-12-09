package com.omasyo.gatherspace.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.omasyo.gatherspace.TextFieldState
import com.omasyo.gatherspace.auth.AuthEvent
import com.omasyo.gatherspace.auth.AuthState
import com.omasyo.gatherspace.auth.LoginViewModelImpl
import com.omasyo.gatherspace.auth.SignupViewModelImpl
import com.omasyo.gatherspace.components.layouts.FormLayout
import com.omasyo.gatherspace.components.widgets.TextField
import com.omasyo.gatherspace.styles.lightDark
import com.omasyo.gatherspace.theme.primaryDark
import com.omasyo.gatherspace.theme.primaryLight
import com.omasyo.gatherspace.viewmodels.domainComponent
import com.varabyte.kobweb.core.Page
import kotlinx.browser.window
import org.jetbrains.compose.web.attributes.rows
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

val loginViewModel = with(domainComponent) {
    LoginViewModelImpl(authRepository)
}

@Page
@Composable
fun LoginPage() {
    LoginPage(
        username = loginViewModel.usernameField,
        onUsernameChange = loginViewModel::changeUsername,
        password = loginViewModel.passwordField,
        onPasswordChange = loginViewModel::changePassword,
        onSubmit = loginViewModel::submit,
        state = loginViewModel.state.collectAsState().value,
        onEventReceived = { loginViewModel.clearEvent() },
    )
}

@Composable
fun LoginPage(
    username: TextFieldState,
    onUsernameChange: (String) -> Unit,
    password: TextFieldState,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    state: AuthState,
    onEventReceived: (AuthEvent) -> Unit,
) {
    FormLayout("Login") {
        H1 {
            Text("Login to your account")
        }
        TextField(
            value = username.value,
            onValueChange = { onUsernameChange(it) },
            placeholder = "Username",
        )
        TextField(
            value = password.value,
            onValueChange = { onPasswordChange(it) },
            placeholder = "Password",
            isPassword = true,
        )
        Button(
            attrs = {
                onClick { onSubmit() }
            }
        ) {
            Text("Login")
        }
        H5(
            attrs = {
                style {
                    textAlign("center")
                }
            }
        ) {
            Text("Don't have an account? ")
            A(
                href = "/signup",
                attrs = {
                    style {
                        color(lightDark(primaryLight, primaryDark))
                    }
                }
            ) {
                Text("Register")
            }
        }
    }


    LaunchedEffect(state) {
        when (val event = state.event) {
            is AuthEvent.Error -> {
                //TODO
//                snackbarHostState.showSnackbar(event.message)
            }

            AuthEvent.Success -> {
                window.location.href = "/"
            }

            AuthEvent.None -> Unit
        }
        onEventReceived(state.event)
    }
}

