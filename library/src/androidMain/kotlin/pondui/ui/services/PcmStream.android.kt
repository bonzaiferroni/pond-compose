package pondui.ui.services

actual class PcmStream actual constructor(sampleRate: Int, channels: Int, capacity: Int) {
    actual fun start() {
    }

    actual suspend fun send(chunk: ByteArray) {
    }

    actual fun trySend(chunk: ByteArray): Boolean {
        TODO("Not yet implemented")
    }

    actual suspend fun finish() {
    }
}