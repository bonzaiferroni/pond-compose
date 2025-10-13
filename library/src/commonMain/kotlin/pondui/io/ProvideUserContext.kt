package pondui.io

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pondui.ui.modifiers.onEnterPressed
import pondui.ui.theme.Pond
import pondui.ui.controls.*

@Composable
fun ProvideUserContext(
    apiClient: NeoApiClient,
    userContext: UserContext = viewModel { UserContext(apiClient) },
    block: @Composable () -> Unit
) {
    val state by userContext.stateFlow.collectAsState()

    Cloud(
        isVisible = state.dialogVisible, toggle = userContext::toggle,
    ) {
        Column(
            gap = 2,
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
            Button("Cancel", onClick = dismiss, color = Pond.colors.secondary, modifier = Modifier.weight(1f))
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
    Column(2, modifier = Modifier.width(250.dp)) {
        TextField(
            text = usernameOrEmail, onChange = setUsernameOrEmail,
            label = "username",
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            text = password, onChange = setPassword, hideCharacters = true,
            label = "password",
            modifier = Modifier.fillMaxWidth()
                .onEnterPressed(login)
        )
        Column(1) {
            Checkbox(
                value = saveLogin,
                onValueChanged = setSaveLogin,
                label = "Save username",
            )
            Checkbox(
                value = stayLoggedIn,
                onValueChanged = setStayLoggedIn,
                label = "Stay logged in",
            )
        }
        Row(1) {
            Button(
                text = "Log in", onClick = login, modifier = Modifier.weight(1f),
            )
            Button(
                text = "Cancel", onClick = dismiss, modifier = Modifier.weight(1f),
                color = Pond.colors.secondary
            )
        }
    }
}

val LocalUserContext = staticCompositionLocalOf<UserContext?> {
    null
}

@Composable
fun ProvidableCompositionLocal<UserContext?>.collectState(): State<UserContextState> {
    val current = this.current
    return current?.stateFlow?.collectAsState() ?: derivedStateOf { UserContextState() }
}