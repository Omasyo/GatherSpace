package com.omasyo.gatherspace.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.omasyo.gatherspace.TextFieldState
import com.omasyo.gatherspace.auth.AuthEvent
import com.omasyo.gatherspace.auth.AuthState
import com.omasyo.gatherspace.auth.SignupViewModelImpl
import com.omasyo.gatherspace.components.layouts.FormLayout
import com.omasyo.gatherspace.components.layouts.showSnackbar
import com.omasyo.gatherspace.components.widgets.TextField
import com.omasyo.gatherspace.styles.MainStyle
import com.omasyo.gatherspace.styles.lightDark
import com.omasyo.gatherspace.theme.primaryDark
import com.omasyo.gatherspace.theme.primaryLight
import com.omasyo.gatherspace.viewmodels.domainComponent
import com.varabyte.kobweb.core.Page
import kotlinx.browser.window
import org.jetbrains.compose.web.attributes.rows
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

val signupViewModel = with(domainComponent) {
    SignupViewModelImpl(authRepository, userRepository)
}

@Page
@Composable
fun SignupPage() {
    SignupPage(
        username = signupViewModel.usernameField,
        onUsernameChange = signupViewModel::changeUsername,
        password = signupViewModel.passwordField,
        onPasswordChange = signupViewModel::changePassword,
        onSubmit = signupViewModel::submit,
        state = signupViewModel.state.collectAsState().value,
        onEventReceived = { signupViewModel.clearEvent() },
    )
}

@Composable
fun SignupPage(
    username: TextFieldState,
    onUsernameChange: (String) -> Unit,
    password: TextFieldState,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    state: AuthState,
    onEventReceived: (AuthEvent) -> Unit,
) {
    FormLayout("Register") {
        H1 {
            Text("Create an account")
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
            Text("Register")
        }
        H5(
            attrs = {
                style {
                    textAlign("center")
                }
            }
        ) {
            Text("Already have an account? ")
            A(
                href = "/login",
                attrs = {
                    classes(MainStyle.filledButton)
                    style {
                        color(lightDark(primaryLight, primaryDark))
                    }
                }
            ) {
                Text("Login")
            }
        }
    }


    LaunchedEffect(state) {
        when (val event = state.event) {
            is AuthEvent.Error -> {
                showSnackbar(event.message)
            }

            AuthEvent.Success -> {
                window.location.href = "/"
            }

            AuthEvent.None -> Unit
        }
        onEventReceived(state.event)
    }
}

