package pondui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.StateFlow

//@Composable
//fun <T> rememberNotNull(input: T?, initialValue: T): StateFlow<T> {
//    val cached = remember { mutableStateOf(initialValue) }
//    if (input != null) {
//        cached.value = input
//    }
//    return cached
//}