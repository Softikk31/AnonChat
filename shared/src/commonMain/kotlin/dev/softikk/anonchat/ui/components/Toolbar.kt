package dev.softikk.anonchat.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonShapes
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import anonchat.shared.generated.resources.Res
import anonchat.shared.generated.resources.label_text_field_enter_message
import dev.softikk.anonchat.ui.theme.Dimens
import dev.softikk.anonchat.ui.theme.textIcon
import org.jetbrains.compose.resources.stringResource

private val HeightTextField = 52.dp
private val SizeButtonSendMessage = 52.dp
private val WidthStrokeToolbar = 1.5.dp
private const val IconSendMessage = '>'

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Toolbar(
    state: TextFieldState
) {
    val focusRequester = remember { FocusRequester() }

    val shape = RoundedCornerShape(Dimens.shapeSmall)

    Box(modifier = Modifier.fillMaxWidth().height(HeightTextField)) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
        ) {
            BasicTextField(
                modifier = Modifier.fillMaxSize().focusable().focusRequester(focusRequester),
                state = state,
                decorator = TextFieldDefaults.decorator(
                    state = state,
                    enabled = true,
                    lineLimits = TextFieldLineLimits.MultiLine(maxHeightInLines = 6),
                    outputTransformation = null,
                    interactionSource = remember { MutableInteractionSource() },
                    label = {
                        Text(
                            text = stringResource(Res.string.label_text_field_enter_message),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
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

            Button(modifier = Modifier.size(SizeButtonSendMessage), onClick = {

            }, shapes = ButtonShapes(shape, shape), content = {
                Text(
                    text = IconSendMessage.toString(),
                    style = MaterialTheme.typography.textIcon,
                    color = MaterialTheme.colorScheme.onSurface
                )
            })
        }
    }
}