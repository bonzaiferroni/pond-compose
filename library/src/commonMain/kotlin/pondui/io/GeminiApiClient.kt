package pondui.io

import kabinet.clients.GeminiMessage
import kabinet.gemini.GeminiApi
import kabinet.model.ImageGenRequest
import kabinet.model.ImageUrls
import kabinet.model.SpeechRequest

interface GeminiAppClient {
    suspend fun chat(messages: List<GeminiMessage>): String?
    suspend fun image(request: ImageGenRequest): ImageUrls?
    suspend fun generateSpeechUrl(request: SpeechRequest): String?
    suspend fun generateSpeech(request: SpeechRequest): ByteArray?
}

class GeminiApiClient(
    private val geminiApi: GeminiApi,
    private val client: NeoApiClient
): GeminiAppClient {
    override suspend fun chat(messages: List<GeminiMessage>) = client.request(geminiApi.chat, messages)
    override suspend fun image(request: ImageGenRequest) = client.request(geminiApi.image, request)
    override suspend fun generateSpeechUrl(request: SpeechRequest) = client.request(geminiApi.speechUrl, request)
    override suspend fun generateSpeech(request: SpeechRequest) = client.request(geminiApi.speech, request)
}

class GeminiMockClient: GeminiAppClient {
    override suspend fun chat(messages: List<GeminiMessage>) = "Hello world!"
    override suspend fun image(request: ImageGenRequest) = ImageUrls(
        url = "https://via.placeholder.com/512.png?text=Image+Placeholder",
        thumbUrl = "https://via.placeholder.com/128.png?text=Thumb+Placeholder"
    )
    override suspend fun generateSpeechUrl(request: SpeechRequest) = null
    override suspend fun generateSpeech(request: SpeechRequest) = null
}