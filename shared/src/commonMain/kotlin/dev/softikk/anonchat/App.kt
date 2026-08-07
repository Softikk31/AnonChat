package dev.softikk.anonchat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import anonchat.shared.generated.resources.Res
import anonchat.shared.generated.resources.main_room_description
import dev.softikk.anonchat.ui.components.MessageBox
import dev.softikk.anonchat.ui.components.ToolBar
import dev.softikk.anonchat.ui.components.TopBar
import dev.softikk.anonchat.ui.components.TopBarMobile
import dev.softikk.anonchat.ui.theme.AnonChatTheme
import dev.softikk.anonchat.ui.theme.Dimens
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
import org.jetbrains.compose.resources.stringResource
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.JsModule
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
                    protocol = if (IS_SSL) URLProtocol.HTTPS else URLProtocol.HTTP
                }
            }
            install(WebSockets) {
                contentConverter = KotlinxWebsocketSerializationConverter(Json)
            }
        })
    }
    val messages by chat.messages.collectAsState()
    val online by chat.online.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    val textFieldState = rememberTextFieldState()

    AnonChatTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.surface,
            topBar = {
                if (MobileJs.isMobile()) {
                    TopBarMobile(
                        screenName = "/chat",
                        screenDescription = stringResource(Res.string.main_room_description),
                        onlineCount = online
                    )
                } else {
                    TopBar(
                        screenName = "/chat",
                        screenDescription = stringResource(Res.string.main_room_description),
                        onlineCount = online
                    )
                }
            },
            bottomBar = {
                ToolBar(
                    state = textFieldState
                ) {
                    val text = textFieldState.text.toString()
                    chat.sendMessage(text)
                    textFieldState.edit {
                        delete(0, text.length)
                    }
                }
            }) { paddingValues ->
            LazyColumn(
                modifier = Modifier.padding(paddingValues).padding(start = Dimens.mediumPadding).fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(Dimens.largePadding)
            ) {
                items(messages) { messageModel ->
                    val localDateTime =
                        messageModel.createAt.toInstant(TimeZone.UTC).toLocalDateTime(
                            TimeZone.currentSystemDefault()
                        )
                    MessageBox(
                        anonName = messageModel.anonName,
                        sentAt = "${localDateTime.time.hour.dateFormatter()}:${localDateTime.time.minute.dateFormatter()} ${localDateTime.day.dateFormatter()}.${localDateTime.month.number.dateFormatter()}.${localDateTime.year}",
                        text = messageModel.text,
                        anonNameColor = messageModel.anonNameColor
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalWasmJsInterop::class)
@JsModule("./isMobile.js")
external object MobileJs {
    fun isMobile(): Boolean
}
fun Int.dateFormatter(): String = if (this.toString().length == 1) "0$this" else this.toString()