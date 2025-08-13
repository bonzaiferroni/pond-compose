package pondui.io

import kabinet.clients.GeminiMessage
import kabinet.gemini.GeminiApi
import kabinet.model.ImageGenRequest
import kabinet.model.SpeechGenRequest

class GeminiApiClient(
    private val geminiApi: GeminiApi,
    private val client: ApiClient = globalApiClient
) {
    suspend fun chat(messages: List<GeminiMessage>) = client.post(geminiApi.chat, messages)

    suspend fun image(request: ImageGenRequest) = client.post(geminiApi.image, request)

    suspend fun generateSpeech(request: SpeechGenRequest) = client.post(geminiApi.speech, request)
}