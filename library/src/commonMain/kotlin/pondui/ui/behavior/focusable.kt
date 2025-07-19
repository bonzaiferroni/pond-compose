package pondui.ui.behavior

import androidx.compose.foundation.focusable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged

@Composable
fun Modifier.focusable(
    onFocusChanged: (FocusState) -> Unit
): Modifier {
    val focusRequester = remember { FocusRequester() }

    return this.focusRequester(focusRequester)
        .focusable()
        .onFocusChanged(onFocusChanged)
}