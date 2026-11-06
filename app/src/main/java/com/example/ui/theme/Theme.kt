package com.example.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

enum class BibleTheme {
    MIDNIGHT_BIBLE,
    PARCHMENT_IVORY,
    ROYAL_BURGUNDY
}

private val MidnightBibleScheme = darkColorScheme(
    primary = SacredGold,
    onPrimary = MidnightNavy,
    primaryContainer = AntiqueGold,
    onPrimaryContainer = MidnightDark,
    secondary = BrightGold,
    onSecondary = MidnightNavy,
    tertiary = ScriptureCrimson,
    onTertiary = Color.White,
    background = MidnightNavy,
    onBackground = IvoryText,
    surface = MidnightSurface,
    onSurface = IvoryText,
    surfaceVariant = MidnightCard,
    onSurfaceVariant = SoftGoldText,
    outline = SacredGold.copy(alpha = 0.5f),
    outlineVariant = MidnightBorder
)

private val ParchmentIvoryScheme = lightColorScheme(
    primary = AntiqueGold,
    onPrimary = Color.White,
    primaryContainer = ParchmentWarm,
    onPrimaryContainer = InkBlack,
    secondary = DeepGold,
    onSecondary = Color.White,
    tertiary = ScriptureCrimson,
    onTertiary = Color.White,
    background = ParchmentLight,
    onBackground = InkBlack,
    surface = ParchmentWarm,
    onSurface = InkBlack,
    surfaceVariant = ParchmentSurface,
    onSurfaceVariant = InkMuted,
    outline = AntiqueGold.copy(alpha = 0.6f),
    outlineVariant = ParchmentBorder
)

private val RoyalBurgundyScheme = darkColorScheme(
    primary = BrightGold,
    onPrimary = RoyalBurgundyDark,
    primaryContainer = SacredGold,
    onPrimaryContainer = RoyalBurgundyDark,
    secondary = AntiqueGold,
    onSecondary = RoyalBurgundyDark,
    tertiary = BrightGold,
    onTertiary = RoyalBurgundyDark,
    background = RoyalBurgundyDark,
    onBackground = IvoryText,
    surface = RoyalBurgundySurface,
    onSurface = IvoryText,
    surfaceVariant = RoyalBurgundyCard,
    onSurfaceVariant = SoftGoldText,
    outline = SacredGold.copy(alpha = 0.5f),
    outlineVariant = RoyalBurgundyBorder
)

@Composable
fun HolyHomeTheme(
    themeMode: BibleTheme = BibleTheme.MIDNIGHT_BIBLE,
    content: @Composable () -> Unit
) {
    val colorScheme: ColorScheme = when (themeMode) {
        BibleTheme.MIDNIGHT_BIBLE -> MidnightBibleScheme
        BibleTheme.PARCHMENT_IVORY -> ParchmentIvoryScheme
        BibleTheme.ROYAL_BURGUNDY -> RoyalBurgundyScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = HolyHomeTypography,
        content = content
    )
}
