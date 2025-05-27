package pondui.ui.behavior

import androidx.compose.foundation.focusable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type

@Composable
fun Modifier.onEnterPressed(block: () -> Unit) = this.onPreviewKeyEvent { event ->
    val enterPressed = event.key == Key.Enter || event.key == Key.NumPadEnter
    val isDownPress = event.type == KeyEventType.KeyDown
    if (enterPressed && isDownPress && !event.isShiftPressed) block()
    enterPressed
}

@Composable
fun Modifier.takeInitialFocus(): Modifier {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }
    return this.focusable()
        .focusRequester(focusRequester)
}