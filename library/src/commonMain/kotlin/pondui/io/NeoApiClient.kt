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
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import kabinet.api.Endpoint
import kabinet.api.GetByTableIdEndpoint
import kabinet.api.UserApi
import kabinet.db.TableId
import kabinet.model.Auth
import kabinet.model.LoginRequest
import pondui.APP_API_HOST
import pondui.APP_API_PORT
import pondui.APP_API_PROTOCOL

class NeoApiClient(
    val apiProtocol: URLProtocol = APP_API_PROTOCOL,
    val apiHost: String = APP_API_HOST,
    val apiPort: Int = APP_API_PORT,
    val client: HttpClient = globalKtorClient
) {
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
            url {
                protocol = apiProtocol
                host = apiHost
                port = apiPort
                pathSegments = endpoint.pathSegments
            }
            contentType(ContentType.Application.Json)
            jwt?.let {
                header(HttpHeaders.Authorization, "Bearer $it")
            }
            body?.let { setBody(it) }
        }
        request.block(endpoint)

        val response = client.request(request)
        if (response.status == HttpStatusCode.Unauthorized && loginRequest != null) {
            val auth = login()
            if (auth != null) {
                return requestResponse(endpoint, body, block)
            }
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
                url {
                    protocol = apiProtocol
                    host = apiHost
                    port = apiPort
                    pathSegments = UserApi.Login.pathSegments
                }
                setBody(loginRequest)
            }
            if (response.status != HttpStatusCode.OK) {
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
}

val globalNeoApiClient = NeoApiClient()