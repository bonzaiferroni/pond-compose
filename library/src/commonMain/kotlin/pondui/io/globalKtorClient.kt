package pondui.io

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

val globalKtorClient = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
    defaultRequest {
        headers {
            set(HttpHeaders.ContentType, "application/json")
        }
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 120_000 // Set sendRequest timeout
        connectTimeoutMillis = 120_000 // Set connection timeout
        socketTimeoutMillis = 120_000  // Set socket timeout
    }
}