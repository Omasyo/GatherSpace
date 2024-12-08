package com.omasyo.gatherspace.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.omasyo.gatherspace.TextFieldState
import com.omasyo.gatherspace.auth.AuthEvent
import com.omasyo.gatherspace.auth.AuthState
import com.omasyo.gatherspace.auth.SignupViewModelImpl
import com.omasyo.gatherspace.components.layouts.FormLayout
import com.omasyo.gatherspace.components.widgets.TextField
import com.omasyo.gatherspace.viewmodels.domainComponent
import com.varabyte.kobweb.core.Page
import org.jetbrains.compose.web.attributes.rows
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.TextArea
import org.jetbrains.compose.web.dom.TextInput

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
    }
}

