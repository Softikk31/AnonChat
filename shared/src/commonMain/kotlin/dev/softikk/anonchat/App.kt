package dev.softikk.anonchat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.kotlinx.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
data class Message(
    val id: Uuid, val text: String, val createAt: LocalDateTime
)

class ChatService(
    private val client: HttpClient
) : ViewModel() {
    private var session: DefaultWebSocketSession? = null
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages.asStateFlow()

    init {
        viewModelScope.launch {
            client.webSocket("wss://4-chan.ru/chat") {
                session = this
                while (isActive) {
                    _messages.value = receiveDeserialized<List<Message>>()
                }
            }
        }
    }

    fun sendMessage(text: String) {
        viewModelScope.launch {
            session?.send(text)
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalUuidApi::class)
@Composable
@Preview
fun App() {
    val chat = viewModel {
        ChatService(HttpClient {
            install(WebSockets) {
                contentConverter = KotlinxWebsocketSerializationConverter(Json)
            }
        })
    }
    val messages by chat.messages.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    val textFieldState = rememberTextFieldState()
    Box(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(), state = listState, verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = messages, key = { message ->
                    message.id
                }) { message ->
                Column {
                    Text(
                        text = message.text
                    )
                    val dateTime =
                        message.createAt.toInstant(TimeZone.UTC).toLocalDateTime(TimeZone.currentSystemDefault())
                    Text(
                        text = "${dateTime.hour}:${dateTime.minute}, ${dateTime.date.day}.${dateTime.month.number}.${dateTime.year}"
                    )
                }
            }
            item {
                Spacer(Modifier.height(48.dp))
            }
        }

        Row(
            modifier = Modifier.align(Alignment.BottomStart),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.height(48.dp).weight(1f),
                state = textFieldState,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    selectionColors = TextSelectionColors(
                        handleColor = Color.Black, backgroundColor = Color.Black.copy(0.1f)
                    ),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black
                )
            )
            Button(
                modifier = Modifier.height(48.dp), colors = ButtonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White,
                    disabledContainerColor = Color.Black,
                    disabledContentColor = Color.White
                ), onClick = {
                    val text = textFieldState.text.toString()
                    chat.sendMessage(text)
                    textFieldState.edit {
                        delete(0, text.length)
                    }
                }, shapes = ButtonShapes(
                    shape = RoundedCornerShape(8.dp), pressedShape = RoundedCornerShape(8.dp)
                ), content = {
                    Text(
                        text = "Input"
                    )
                })
        }
    }
}