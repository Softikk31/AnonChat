package dev.softikk.anonchat.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.softikk.anonchat.ui.theme.AnonNameColors
import dev.softikk.anonchat.ui.theme.Dimens

private val MaxWidthMessageBox = 800.dp
private val WidthInfo = 208.dp

@Composable
fun MessageBox(anonName: String, sentAt: String, text: String) {
    Box(
        modifier = Modifier.widthIn(max = MaxWidthMessageBox)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimens.shapeSmall),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.width(WidthInfo),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = anonName,
                    style = MaterialTheme.typography.bodySmall,
                    color = AnonNameColors.random()
                )
                Text(
                    text = sentAt,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}