package dev.softikk.anonchat

const val HOST = "4-chan.ru"
const val IS_DEBUG = false
const val IS_SSL = true
val PORT = if (IS_SSL) 443 else 8080
const val DEBUG_NAME = "AnonChat"