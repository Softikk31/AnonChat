package dev.softikk.anonchat.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonShapes
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import anonchat.shared.generated.resources.Res
import anonchat.shared.generated.resources.label_text_field_enter_message
import dev.softikk.anonchat.ui.theme.Dimens
import dev.softikk.anonchat.ui.theme.textIcon
import org.jetbrains.compose.resources.stringResource

private val HeightTextField = 52.dp
private val SizeButtonSendMessage = 52.dp
private val WidthStrokeToolbar = 1.5.dp
private val ContentVerticalPaddingTextField = 14.dp
private const val IconSendMessage = '>'

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ToolBar(
    state: TextFieldState, sendMessage: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    val shape = RoundedCornerShape(Dimens.shapeSmall)

    Box(
        modifier = Modifier.padding(horizontal = Dimens.mediumPadding)
            .padding(bottom = Dimens.mediumPadding)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
        ) {
            CompositionLocalProvider(
                LocalTextSelectionColors provides TextSelectionColors(
                    handleColor = MaterialTheme.colorScheme.onSurface,
                    backgroundColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                )
            ) {
                BasicTextField(
                    modifier = Modifier.weight(1f).heightIn(min = HeightTextField).focusable()
                        .focusRequester(focusRequester),
                    state = state,
                    textStyle = MaterialTheme.typography.bodySmall.copy(MaterialTheme.colorScheme.onSurface),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                    lineLimits = TextFieldLineLimits.MultiLine(maxHeightInLines = 6),
                    decorator = TextFieldDefaults.decorator(
                        state = state,
                        enabled = true,
                        lineLimits = TextFieldLineLimits.MultiLine(maxHeightInLines = 6),
                        outputTransformation = null,
                        interactionSource = remember { MutableInteractionSource() },
                        contentPadding = PaddingValues(
                            horizontal = Dimens.mediumPadding,
                            vertical = ContentVerticalPaddingTextField
                        ),
                        placeholder = {
                            Text(
                                text = stringResource(Res.string.label_text_field_enter_message),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            cursorColor = MaterialTheme.colorScheme.onSurface,
                            selectionColors = TextSelectionColors(
                                handleColor = MaterialTheme.colorScheme.onSurface,
                                backgroundColor = MaterialTheme.colorScheme.onSurface.copy(0.1f)
                            )
                        ),
                        container = {
                            Box(
                                modifier = Modifier.fillMaxSize().border(
                                    width = WidthStrokeToolbar,
                                    shape = shape,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        })
                )
            }

            Button(
                modifier = Modifier.size(SizeButtonSendMessage),
                colors = ButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = MaterialTheme.colorScheme.onSurface
                ),
                border = BorderStroke(
                    width = WidthStrokeToolbar, color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                onClick = sendMessage,
                enabled = state.text.toString().isNotBlank(),
                shapes = ButtonShapes(shape, shape), content = {
                    Text(
                        text = IconSendMessage.toString(),
                        style = MaterialTheme.typography.textIcon,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                })
        }
    }
}