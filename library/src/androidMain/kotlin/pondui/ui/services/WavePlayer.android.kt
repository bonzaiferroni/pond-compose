package pondui.ui.services

actual class WavePlayer {
    actual suspend fun play(url: String) {
    }

    actual suspend fun play(bytes: ByteArray) {
    }

    actual fun getClip(bytes: ByteArray): WaveClip {
        TODO("Not yet implemented")
    }

    actual fun readInfo(bytes: ByteArray): Int? {
        TODO("Not yet implemented")
    }
}