package pondui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.StateFlow

@Composable
fun <T> rememberLastNonNull(value: T?): T? {
    var last by remember { mutableStateOf(value) }
    last = value ?: last
    return last
}