package pondui.io

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
        isVisible = state.loginVisible, onDismiss = userContext::dismissLogin,
        modifier = Modifier.width(250.dp)
    ) {
        Column(
            verticalArrangement = Pond.ruler.columnTight
        ) {
            ControlColumn {
                TextField(
                    text = state.usernameOrEmail, onTextChange = userContext::setUsernameOrEmail,
                    placeholder = "Username",
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    text = state.password, onTextChange = userContext::setPassword, hideCharacters = true,
                    placeholder = "Password",
                    modifier = Modifier.fillMaxWidth()
                )
            }
            LabelCheckbox(
                value = state.saveLogin,
                onValueChanged = userContext::setSaveLogin,
                label = "Save username",
            )
            LabelCheckbox(
                value = state.stayLoggedIn,
                onValueChanged = userContext::setStayLoggedIn,
                label = "Stay logged in",
            )
            ControlRow {
                TextButton(
                    text = "Log in", onClick = userContext::login, modifier = Modifier.weight(1f),
                )
                TextButton(
                    text = "Cancel", onClick = userContext::dismissLogin, modifier = Modifier.weight(1f),
                    background = Pond.colors.secondary
                )
            }
        }
    }

    CompositionLocalProvider(LocalUserContext provides userContext) {
        block()
    }
}

val LocalUserContext = staticCompositionLocalOf<UserContext> {
    error("No Nav provided")
}