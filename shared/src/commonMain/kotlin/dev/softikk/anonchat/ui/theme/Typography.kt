package dev.softikk.anonchat.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import anonchat.shared.generated.resources.Res
import anonchat.shared.generated.resources.jet_brains_mono
import org.jetbrains.compose.resources.Font

val jetBrainsMono: FontFamily
    @Composable get() = FontFamily(Font(Res.font.jet_brains_mono))

val Typography: Typography
    @Composable get() = Typography(
        labelLarge = TextStyle(
            fontSize = 15.sp, fontFamily = jetBrainsMono
        ), bodySmall = TextStyle(
            fontSize = 16.sp, fontFamily = jetBrainsMono
        ), bodyMedium = TextStyle(
            fontSize = 16.sp, fontFamily = jetBrainsMono, fontWeight = FontWeight.Medium
        ), bodyLarge = TextStyle(
            fontSize = 20.sp, fontFamily = jetBrainsMono
        ), headlineMedium = TextStyle(
            fontSize = 20.sp, fontFamily = jetBrainsMono, fontWeight = FontWeight.Medium
        )
    )

val Typography.textIcon: TextStyle
    @Composable get() = TextStyle(
        fontSize = 20.sp, fontFamily = jetBrainsMono
    )