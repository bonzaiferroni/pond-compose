package pondui.utils

import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.time.Duration

data class GeoLocation(
    val latitude: Double,
    val longitude: Double,
    val accuracyMeters: Double? = null,
    val provider: String? = null,
    val timestampMillis: Long = System.currentTimeMillis()
)

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class GeoLocator {
    /**
     * One-shot current position. Returns null if unavailable.
     */
    suspend fun current(timeout: Duration? = null): GeoLocation?

    /**
     * Polling watcher. Calls onUpdate when a new fix arrives.
     * Returns a handle you can close() to stop.
     */
    fun watch(
        interval: Duration,
        onUpdate: (GeoLocation) -> Unit
    ): AutoCloseable
}

@Composable
expect fun rememberGeoLocator(): GeoLocator?

fun GeoLocator.current(timeout: Duration? = null, callback: (GeoLocation?) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        val location = current(timeout)
        callback(location)
    }
}