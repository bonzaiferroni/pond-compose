package pondui.io

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pondui.ui.behavior.onEnterPressed
import pondui.ui.theme.Pond
import pondui.ui.controls.*

@Composable
fun ProvideUserContext(
    userContext: UserContext = viewModel { UserContext() },
    block: @Composable () -> Unit
) {
    val state by userContext.state.collectAsState()

    Cloud(
        isVisible = state.dialogVisible, onDismiss = userContext::toggle,
        modifier = Modifier.width(250.dp)
    ) {
        Column(
            verticalArrangement = Pond.ruler.columnUnit
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
        block()
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LogoutControl(
    username: String,
    logout: () -> Unit,
    dismiss: () -> Unit,
) {
    Column(
        verticalArrangement = Pond.ruler.columnUnit,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Logged in as $username.")
        ControlSet {
            Button("Logout", onClick = logout, modifier = Modifier.weight(1f))
            Button("Cancel", onClick = dismiss, background = Pond.colors.tertiary, modifier = Modifier.weight(1f))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
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
    ControlSet(maxItemsInEachRow = 1) {
        TextField(
            text = usernameOrEmail, onTextChanged = setUsernameOrEmail,
            placeholder = "Username",
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            text = password, onTextChanged = setPassword, hideCharacters = true,
            placeholder = "Password",
            modifier = Modifier.fillMaxWidth()
                .onEnterPressed(login)
        )
    }
    LabeledCheckbox(
        value = saveLogin,
        onValueChanged = setSaveLogin,
        label = "Save username",
    )
    LabeledCheckbox(
        value = stayLoggedIn,
        onValueChanged = setStayLoggedIn,
        label = "Stay logged in",
    )
    ControlSet {
        Button(
            text = "Log in", onClick = login, modifier = Modifier.weight(1f),
        )
        Button(
            text = "Cancel", onClick = dismiss, modifier = Modifier.weight(1f),
            background = Pond.colors.tertiary
        )
    }
}

val LocalUserContext = staticCompositionLocalOf<UserContext?> {
    null
}

@Composable
fun ProvidableCompositionLocal<UserContext?>.collectState(): State<UserContextState> {
    val current = this.current
    return current?.state?.collectAsState() ?: derivedStateOf { UserContextState() }
}