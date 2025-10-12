package pondui.ui.services

expect class PcmStream(
    sampleRate: Int = 16000,
    channels: Int = 1,
    capacity: Int = 32
) {
    fun start()
    suspend fun send(chunk: ByteArray)
    fun trySend(chunk: ByteArray): Boolean
    suspend fun finish()
}