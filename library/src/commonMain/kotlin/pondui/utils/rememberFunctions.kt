package pondui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun <T> rememberNotNull(key: Any?, value: T?): T? {
    var last by remember(key) { mutableStateOf(value) }
    last = value ?: last
    return last
}