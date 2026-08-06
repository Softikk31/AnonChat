package dev.softikk.anonchat.dto

import dev.softikk.anonchat.models.MessageModel
import kotlinx.serialization.Serializable

@Serializable
data class ChatRespondDto(
    val messages: List<MessageModel>,
    val online: Int
)