package dev.softikk.anonchat.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

val lightColorScheme = lightColorScheme(
    surface = White, onSurface = Black, onSurfaceVariant = Gray
)

val darkColorScheme = darkColorScheme(
    surface = Black, onSurface = White, onSurfaceVariant = LightGray
)

@Composable
fun AnonChatTheme(content: @Composable () -> Unit) {
    val isDarkTheme = false
    MaterialTheme(
        colorScheme = if (isDarkTheme) darkColorScheme else lightColorScheme,
        typography = Typography,
        content = content
    )
}