package pondui.io

import kabinet.clients.GeminiMessage
import kabinet.gemini.GeminiApi
import kabinet.model.ImageGenRequest
import kabinet.model.SpeechRequest

class GeminiApiClient(
    private val geminiApi: GeminiApi,
    private val client: NeoApiClient = globalNeoApiClient
) {
    suspend fun chat(messages: List<GeminiMessage>) = client.request(geminiApi.chat, messages)

    suspend fun image(request: ImageGenRequest) = client.request(geminiApi.image, request)

    suspend fun generateSpeech(request: SpeechRequest) = client.request(geminiApi.speech, request)
}