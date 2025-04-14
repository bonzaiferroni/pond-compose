package io.pond.compose

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform