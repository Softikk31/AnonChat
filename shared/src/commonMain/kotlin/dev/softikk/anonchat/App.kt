package dev.softikk.anonchat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonShapes
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.softikk.anonchat.viewmodel.ChatViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalUuidApi::class)
@Composable
fun App() {
    val chat = viewModel {
        ChatViewModel(HttpClient {
            defaultRequest {
                host = HOST
                port = PORT
                url {
                    protocol = URLProtocol.HTTP
                }
            }
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
            modifier = Modifier.fillMaxSize(),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = messages, key = { message ->
                    message.id
                }) { message ->
                Column {
                    Text(
                        text = message.text
                    )
                    val dateTime = message.createAt.toInstant(TimeZone.UTC)
                        .toLocalDateTime(TimeZone.currentSystemDefault())
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