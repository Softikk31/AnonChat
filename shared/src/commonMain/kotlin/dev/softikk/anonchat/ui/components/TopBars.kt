package dev.softikk.anonchat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import anonchat.shared.generated.resources.Res
import anonchat.shared.generated.resources.online_counter
import anonchat.shared.generated.resources.settings
import anonchat.shared.generated.resources.settings_button_name
import dev.softikk.anonchat.ui.theme.Dimens
import dev.softikk.anonchat.ui.theme.Hacker
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

private val TopBarMobileHeight = 82.dp
private val SettingsIconSize = 24.dp
private val StatusIndicatorSize = 10.dp
private val TopBarHeight = 52.dp

@Composable
private fun TitleTopBar(screenName: String, screenDescription: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.mediumPadding)
    ) {
        Text(
            text = screenName,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = screenDescription,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun OnlineCounter(onlineCount: Int) {
    val onlineCounterText: String = stringResource(Res.string.online_counter, onlineCount)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.smallPadding)
    ) {
        Text(
            text = onlineCounterText,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Box(
            modifier = Modifier.size(StatusIndicatorSize).background(
                color = Hacker, shape = CircleShape
            )
        )
    }
}

@Composable
fun TopBarMobile(
    screenName: String, screenDescription: String, onlineCount: Int
) {
    Box(
        modifier = Modifier.fillMaxWidth().height(TopBarMobileHeight)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(Dimens.mediumPadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.smallPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TitleTopBar(
                    screenName = screenName, screenDescription = screenDescription
                )
                IconButton(modifier = Modifier.size(SettingsIconSize), onClick = {

                }) {
                    Icon(
                        modifier = Modifier.size(SettingsIconSize),
                        imageVector = vectorResource(Res.drawable.settings),
                        contentDescription = stringResource(Res.string.settings_button_name),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            OnlineCounter(
                onlineCount = onlineCount
            )
        }
    }
}

@Composable
fun TopBar(
    screenName: String, screenDescription: String, onlineCount: Int
) {
    Box(
        modifier = Modifier.fillMaxWidth().height(TopBarMobileHeight)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(TopBarHeight)
                .padding(horizontal = Dimens.mediumPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TitleTopBar(
                screenName = screenName, screenDescription = screenDescription
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.largePadding)
            ) {
                OnlineCounter(
                    onlineCount = onlineCount
                )

                Text(
                    text = stringResource(Res.string.settings_button_name),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}