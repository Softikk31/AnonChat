package dev.softikk.anonchat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.softikk.anonchat.DEBUG_NAME
import dev.softikk.anonchat.HOST
import dev.softikk.anonchat.IS_DEBUG
import dev.softikk.anonchat.PORT
import dev.softikk.anonchat.models.Message
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.URLProtocol
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.ClosedSendChannelException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

const val PATH = "/chat"

class ChatViewModel(
    private val client: HttpClient
) : ViewModel() {
    private var session: DefaultClientWebSocketSession? = null
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages.asStateFlow()

    private suspend fun chat() {
        client.webSocket(path = PATH, host = HOST, port = PORT, request = {
            url {
                protocol = URLProtocol.WS
            }
        }) {
            if (session == null) {
                session = this
            }
            try {
                for (frame in incoming) {
                    when (frame) {
                        is Frame.Text -> {
                            _messages.value = Json.decodeFromString<List<Message>>(frame.readText())
                        }

                        is Frame.Close -> {
                            session = null
                            break
                        }

                        else -> {}
                    }
                }
            } catch (e: Exception) {
                if (IS_DEBUG) {
                    print("$DEBUG_NAME: $e")
                }
            } finally {
                session = null
            }
        }
    }

    init {
        viewModelScope.launch {
            try {
                chat()
            } catch (_: ClosedReceiveChannelException) {
                chat()
            } catch (e: Exception) {
                if (IS_DEBUG) {
                    print("$DEBUG_NAME: $e")
                }
            }
        }
    }

    fun sendMessage(text: String) {
        viewModelScope.launch {
            try {
                session?.send(text)
            } catch (_: ClosedSendChannelException) {
                chat()
            }
        }
    }
}