package io.pondlib.compose

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform