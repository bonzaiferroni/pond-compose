package pondui.io

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pondui.ui.theme.Pond
import pondui.ui.controls.*

@Composable
fun ProvideUserContext(
    userContext: UserContext = viewModel { UserContext() },
    block: @Composable () -> Unit
) {
    val state by userContext.state.collectAsState()

    FloatyBox(
        isVisible = state.dialogVisible, onDismiss = userContext::toggle,
        modifier = Modifier.width(250.dp)
    ) {
        Column(
            verticalArrangement = Pond.ruler.columnTight
        ) {
            val user = state.user
            if (user != null) {
                LogoutControl(
                    username = user.username,
                    logout = userContext::logout,
                    dismiss = userContext::toggle,
                )
            } else {
                LoginControls(
                    usernameOrEmail = state.usernameOrEmail,
                    setUsernameOrEmail = userContext::setUsernameOrEmail,
                    password = state.password,
                    setPassword = userContext::setPassword,
                    saveLogin = state.saveLogin,
                    setSaveLogin = userContext::setSaveLogin,
                    stayLoggedIn = state.stayLoggedIn,
                    setStayLoggedIn = userContext::setStayLoggedIn,
                    login = userContext::login,
                    dismiss = userContext::toggle
                )
            }
        }
    }

    CompositionLocalProvider(LocalUserContext provides userContext) {
        if (state.isLoggingIn) {
            Text("Logging in...")
        } else {
            block()
        }
    }
}

@Composable
fun LogoutControl(
    username: String,
    logout: () -> Unit,
    dismiss: () -> Unit,
) {
    Column(
        verticalArrangement = Pond.ruler.columnTight
    ) {
        Text("Logged in as $username.")
        ControlRow {
            TextButton("Logout", onClick = logout, modifier = Modifier.weight(1f))
            TextButton("Cancel", onClick = dismiss, background = Pond.colors.secondary, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun LoginControls(
    usernameOrEmail: String,
    setUsernameOrEmail: (String) -> Unit,
    password: String,
    setPassword: (String) -> Unit,
    saveLogin: Boolean,
    setSaveLogin: (Boolean) -> Unit,
    stayLoggedIn: Boolean,
    setStayLoggedIn: (Boolean) -> Unit,
    login: () -> Unit,
    dismiss: () -> Unit,
) {
    ControlColumn {
        TextField(
            text = usernameOrEmail, onTextChange = setUsernameOrEmail,
            placeholder = "Username",
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            text = password, onTextChange = setPassword, hideCharacters = true,
            placeholder = "Password",
            modifier = Modifier.fillMaxWidth()
        )
    }
    LabelCheckbox(
        value = saveLogin,
        onValueChanged = setSaveLogin,
        label = "Save username",
    )
    LabelCheckbox(
        value = stayLoggedIn,
        onValueChanged = setStayLoggedIn,
        label = "Stay logged in",
    )
    ControlRow {
        TextButton(
            text = "Log in", onClick = login, modifier = Modifier.weight(1f),
        )
        TextButton(
            text = "Cancel", onClick = dismiss, modifier = Modifier.weight(1f),
            background = Pond.colors.secondary
        )
    }
}

val LocalUserContext = staticCompositionLocalOf<UserContext?> {
    null
}