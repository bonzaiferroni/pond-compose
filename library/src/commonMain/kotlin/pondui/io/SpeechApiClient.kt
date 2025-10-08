package pondui.io

import kabinet.api.SpeechApi
import kabinet.model.SpeechRequest

interface SpeechAppClient {
    suspend fun createWav(request: SpeechRequest): ByteArray?
    suspend fun createUrl(request: SpeechRequest): String?
}

class SpeechApiClient(
    private val api: SpeechApi,
    private val client: NeoApiClient
): SpeechAppClient {
    override suspend fun createWav(request: SpeechRequest) = client.request(api.wav, request)
    override suspend fun createUrl(request: SpeechRequest) = client.request(api.url, request)
}

class SpeechMockClient: SpeechAppClient {
    override suspend fun createWav(request: SpeechRequest) = null
    override suspend fun createUrl(request: SpeechRequest) = null
}