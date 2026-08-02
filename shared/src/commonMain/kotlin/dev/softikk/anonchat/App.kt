package dev.softikk.anonchat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class Message(
    val id: Uuid, val text: String, val createAt: LocalDateTime
)

object Database {
    val messages = mutableStateListOf<Message>()
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalUuidApi::class)
@Composable
@Preview
fun App() {
    val messages = Database.messages
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    val textFieldState = rememberTextFieldState()
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState, verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = messages, key = { message ->
                    message.id
                }) { message ->
                Column {
                    Text(
                        text = message.text
                    )
                    val dateTime = message.createAt
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
                modifier = Modifier.height(48.dp), state = textFieldState, colors = OutlinedTextFieldDefaults.colors(
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
                    messages.add(
                        Message(
                            id = Uuid.generateV4(),
                            text = textFieldState.text.toString(),
                            createAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                        )
                    )
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