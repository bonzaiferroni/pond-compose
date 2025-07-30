package pondui.ui.controls

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.toImmutableList

@Composable
fun FloatFieldMenu(
    value: Float,
    modifier: Modifier = Modifier,
    minWidth: Dp = 72.dp,
    onValueSelected: (Float) -> Unit
) {
    var text by remember { mutableStateOf(value.toString()) }

    LaunchedEffect(Unit) {
        if (valueHistory.size < MAX_VALUE_HISTORY && !valueHistory.contains(text)) {
            valueHistory.add(text)
        }
    }

    fun onTextValue(str: String, addHistory: Boolean) {
        val float = str.toFloatOrNull() ?: return
        if (addHistory && str !in valueHistory) {
            valueHistory.add(str)
            if (valueHistory.size > MAX_VALUE_HISTORY) {
                valueHistory.removeAt(0)
            }
        }
        onValueSelected(float)
    }

    TextFieldMenu(
        text = text,
        provideOptions = { valueHistory },
        onChooseSuggestion = {
            onTextValue(it, true)
        },
        onEnterPressed = {
            onTextValue(text, true)
        },
        onTextChanged = {
            text = it
            onTextValue(it, false)
        },
        minWidth = minWidth,
        modifier = modifier
    ) {
        Text(it)
    }
}

private const val MAX_VALUE_HISTORY: Int = 8

private val valueHistory = mutableListOf<String>()