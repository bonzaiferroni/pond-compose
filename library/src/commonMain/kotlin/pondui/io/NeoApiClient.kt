package pondui.io

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import kabinet.api.Endpoint
import kabinet.api.GetByTableIdEndpoint
import kabinet.api.UserApi
import kabinet.console.globalConsole
import kabinet.db.TableId
import kabinet.model.Auth
import kabinet.model.LoginRequest
import pondui.APP_API_URL
import java.net.URI

private val console = globalConsole.getHandle(NeoApiClient::class)

class NeoApiClient(
    apiUrl: String,
    val client: HttpClient = globalKtorClient
) {
    private val apiProtocol: URLProtocol
    private val apiHost: String
    private val apiPort: Int?

    init {
        val url = URI(apiUrl)
        apiProtocol = if (url.scheme == "http") URLProtocol.HTTP else URLProtocol.HTTPS
        apiHost = url.host
        apiPort = url.port.takeIf { it > 0 }
    }

    companion object {
        var jwt: String? = null
        var loginRequest: LoginRequest? = null
    }

    suspend fun <SentType, ReturnType, EndpointType : Endpoint<SentType, ReturnType>> requestResponse(
        endpoint: EndpointType,
        body: SentType? = null,
        block: HttpRequestBuilder.(EndpointType) -> Unit = { }
    ): HttpResponse {
        val request = HttpRequestBuilder().apply {
            method = endpoint.method ?: error("Endpoint method not found: ${endpoint.path}")
            apiUrlOf(endpoint.pathSegments)
            contentType(ContentType.Application.Json)
            jwt?.let {
                header(HttpHeaders.Authorization, "Bearer $it")
            }
            body?.let { setBody(it) }

            block(endpoint)
        }

        val response = client.request(request)
        if (response.status == HttpStatusCode.Unauthorized && loginRequest != null) {
            val auth = login()
            if (auth != null) {
                return requestResponse(endpoint, body, block)
            } else {
                error("Unable to login")
            }
        } else if (response.status != HttpStatusCode.OK) {
            console.logError("Request failed (${response.status}): ${request.url}")
        }
        return response
    }

    suspend fun <IdType : TableId<*>, SentType, ReturnType, EndpointType : Endpoint<SentType, ReturnType>> requestResponseById(
        endpoint: EndpointType,
        id: IdType,
        block: HttpRequestBuilder.(EndpointType) -> Unit = { }
    ): HttpResponse = requestResponse(endpoint) {
        url {
            pathSegments = url.pathSegments + id.value.toString()
        }
        block(endpoint)
    }

    suspend inline fun <IdType : TableId<*>, reified ReturnType, EndpointType : GetByTableIdEndpoint<IdType, ReturnType>> getById(
        endpoint: EndpointType,
        id: IdType,
        noinline block: HttpRequestBuilder.(EndpointType) -> Unit = { }
    ): ReturnType? = requestResponseById(endpoint, id, block).takeIf { it.status == HttpStatusCode.OK }?.body()

    suspend inline fun <SentType, reified ReturnType, EndpointType : Endpoint<SentType, ReturnType>> request(
        endpoint: EndpointType,
        body: SentType? = null,
        noinline block: HttpRequestBuilder.(EndpointType) -> Unit = { }
    ): ReturnType? = requestResponse(endpoint, body, block).takeIf { it.status == HttpStatusCode.OK }?.body()

    suspend fun login(request: LoginRequest? = null): Auth? {
        try {
            request?.let { loginRequest = it }
            val response = client.post {
                apiUrlOf(UserApi.Login.pathSegments)
                setBody(loginRequest)
            }
            if (response.status != HttpStatusCode.OK) {
                console.logError("Login error: ${response.status}")
                return null
            }
            val auth: Auth = response.body()
            jwt = auth.jwt
            loginRequest = loginRequest?.copy(
                password = null,
                refreshToken = auth.refreshToken
            )
            return auth
        } catch (e: Exception) {
            if (e.message == "Connection refused") return null
            throw e
        }
    }

    fun logout() {
        jwt = null
        loginRequest = null
    }

    private fun HttpRequestBuilder.apiUrlOf(segments: List<String>) {
        url {
            protocol = apiProtocol
            host = apiHost
            apiPort?.let { port = it }
            pathSegments = segments
        }
    }
}

val globalNeoApiClient = NeoApiClient(APP_API_URL)

