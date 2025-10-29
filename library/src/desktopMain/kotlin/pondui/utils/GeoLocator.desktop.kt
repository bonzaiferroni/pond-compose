package pondui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberGeoLocator(): GeoLocator? = remember { GeoLocator() }