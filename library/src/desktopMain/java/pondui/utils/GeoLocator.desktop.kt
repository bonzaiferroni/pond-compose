package pondui.utils

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.time.Duration

actual class GeoLocator {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    actual suspend fun current(timeout: Duration?): GeoLocation? {
        val t = timeout ?: Duration.parse("5s")
        return withTimeoutOrNull(t) {
            runCatching {
                val dto: IpApi = client.get("https://ipapi.co/json/").body()
                val lat = dto.latitude ?: return@runCatching null
                val lon = dto.longitude ?: return@runCatching null
                GeoLocation(
                    latitude = lat,
                    longitude = lon,
                    accuracyMeters = null,
                    provider = "ipapi",
                    timestampMillis = System.currentTimeMillis()
                )
            }.getOrNull()
        }
    }

    actual fun watch(
        interval: Duration,
        onUpdate: (GeoLocation) -> Unit
    ): AutoCloseable {
        val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        val job = scope.launch {
            while (isActive) {
                current()?.let(onUpdate)
                delay(interval)
            }
        }
        return AutoCloseable {
            job.cancel()
            scope.cancel()
        }
    }

    @Serializable
    private data class IpApi(
        val latitude: Double? = null,
        val longitude: Double? = null
    )
}

