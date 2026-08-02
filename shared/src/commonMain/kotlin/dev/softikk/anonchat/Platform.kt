package dev.softikk.anonchat

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform