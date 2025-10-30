package pondui.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.location.*
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull
import pondui.ui.controls.permissionGate
import kotlin.coroutines.resume
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

actual class GeoLocator(private val context: Context) {

    private val client by lazy { LocationServices.getFusedLocationProviderClient(context) }

    /**
     * Tries a recent lastLocation first; if missing/stale, listens once via requestLocationUpdates.
     */
    @SuppressLint("MissingPermission")
    actual suspend fun current(timeout: Duration?): GeoLocation? {
        val t = timeout ?: 5.seconds

        return withTimeoutOrNull(t) {
            // 1) Fast path: last known, accept if recent
//            client.lastLocation.await()?.let { loc ->
//                val ageMs = System.currentTimeMillis() - (loc.time ?: 0L)
//                if (ageMs <= 5_000) {
//                    return@withTimeoutOrNull GeoLocation(
//                        latitude = loc.latitude,
//                        longitude = loc.longitude,
//                        accuracyMeters = loc.accuracy.toDouble(),
//                        provider = loc.provider,
//                        timestampMillis = loc.time
//                    )
//                }
//            }

            // 2) One-shot active update (no CancellationToken)
            suspendCancellableCoroutine<GeoLocation?> { cont ->
                val req = LocationRequest.Builder(1_000L)
                    .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                    .setMaxUpdates(1)
                    .build()

                val cb = object : LocationCallback() {
                    override fun onLocationResult(r: LocationResult) {
                        val loc = r.lastLocation
                        client.removeLocationUpdates(this)
                        if (cont.isActive) {
                            cont.resume(
                                loc?.let {
                                    GeoLocation(
                                        latitude = it.latitude,
                                        longitude = it.longitude,
                                        accuracyMeters = it.accuracy.toDouble(),
                                        provider = it.provider,
                                        timestampMillis = it.time
                                    )
                                }
                            )
                        }
                    }
                }

                client.requestLocationUpdates(req, cb, Looper.getMainLooper())

                cont.invokeOnCancellation {
                    client.removeLocationUpdates(cb)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    actual fun watch(
        interval: Duration,
        onUpdate: (GeoLocation) -> Unit
    ): AutoCloseable {
        val request = LocationRequest.Builder(interval.inWholeMilliseconds)
            .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
            .build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(r: LocationResult) {
                val loc = r.lastLocation ?: return
                onUpdate(
                    GeoLocation(
                        latitude = loc.latitude,
                        longitude = loc.longitude,
                        accuracyMeters = loc.accuracy.toDouble(),
                        provider = loc.provider,
                        timestampMillis = loc.time
                    )
                )
            }
        }

        client.requestLocationUpdates(request, callback, Looper.getMainLooper())
        return AutoCloseable { client.removeLocationUpdates(callback) }
    }
}

@Composable
actual fun rememberGeoLocator(): GeoLocator? = permissionGate(listOf(Manifest.permission.ACCESS_COARSE_LOCATION)) {
    val ctx = LocalContext.current
    remember { GeoLocator(ctx) }
}