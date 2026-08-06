package dev.softikk.anonchat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.softikk.anonchat.DEBUG_NAME
import dev.softikk.anonchat.HOST
import dev.softikk.anonchat.IS_DEBUG
import dev.softikk.anonchat.IS_SSL
import dev.softikk.anonchat.PORT
import dev.softikk.anonchat.dto.ChatRespondDto
import dev.softikk.anonchat.models.MessageModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.URLProtocol
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.channels.ClosedSendChannelException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds

const val PATH = "/chat"

class ChatViewModel(
    private val client: HttpClient
) : ViewModel() {
    private var session: DefaultClientWebSocketSession? = null
    private val _messages = MutableStateFlow<List<MessageModel>>(emptyList())
    val messages = _messages.asStateFlow()

    private val _online = MutableStateFlow(0)
    val online = _online.asStateFlow()

    private suspend fun chat() {
        client.webSocket(path = PATH, host = HOST, port = PORT, request = {
            url {
                protocol = if (IS_SSL) URLProtocol.WSS else URLProtocol.WS
            }
        }) {
            if (IS_DEBUG) println("Успешное подключение")
            if (session == null) {
                session = this
            }
            for (frame in incoming) {
                when (frame) {
                    is Frame.Text -> {
                        val receive = Json.decodeFromString<ChatRespondDto>(frame.readText())
                        _messages.value = receive.messages
                        _online.value = receive.online
                    }

                    is Frame.Close -> {
                        session = null
                        break
                    }

                    else -> {}
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            while (true) {
                try {
                    if (IS_DEBUG) println("Попытка переподключения")
                    chat()
                } catch (e: Throwable) {
                    if (IS_DEBUG) {
                        print("$DEBUG_NAME: $e")
                    }
                } finally {
                    if (session != null) session = null
                }
                delay(3.seconds)
            }
        }
    }

    fun sendMessage(text: String) {
        viewModelScope.launch {
            try {
                session?.send(text)
            } catch (_: ClosedSendChannelException) {
                if (IS_DEBUG) println("Канал для отправки закрыт")
            }
        }
    }
}