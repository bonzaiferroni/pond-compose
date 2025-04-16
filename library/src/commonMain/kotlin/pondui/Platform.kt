package pondui

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform