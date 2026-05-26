package com.example.note.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Champagne,
    onPrimary = OnChampagne,
    primaryContainer = Color(0xFF3D3525),
    onPrimaryContainer = Color(0xFFF5E8CC),
    secondary = SageMuted,
    onSecondary = TextPrimaryDark,
    tertiary = BlushMuted,
    onTertiary = TextPrimaryDark,
    background = InkBackground,
    onBackground = TextPrimaryDark,
    surface = InkSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = InkSurfaceHigh,
    onSurfaceVariant = TextSecondaryDark,
    outline = InkOutline,
    outlineVariant = InkOutlineMuted,
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    surfaceContainerLowest = InkBackground,
    surfaceContainerLow = InkSurface,
    surfaceContainer = InkSurfaceHigh,
    surfaceContainerHigh = InkSurfaceHigher,
    surfaceContainerHighest = Color(0xFF2C2C36),
    inverseSurface = TextPrimaryDark,
    inverseOnSurface = InkBackground,
    inversePrimary = ChampagneMuted
)

private val LightColorScheme = lightColorScheme(
    primary = BronzePrimary,
    onPrimary = BronzeOnPrimary,
    primaryContainer = BronzeContainer,
    onPrimaryContainer = Color(0xFF2A2410),
    secondary = Color(0xFF4A5A42),
    onSecondary = Color(0xFFFFFFFF),
    tertiary = Color(0xFF6B4F4F),
    onTertiary = Color(0xFFFFFFFF),
    background = PaperBackground,
    onBackground = TextPrimaryLight,
    surface = PaperSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = PaperSurfaceVariant,
    onSurfaceVariant = TextSecondaryLight,
    outline = Color(0xFFC9C4BA),
    outlineVariant = Color(0xFFE0DBD0),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    surfaceContainerLowest = PaperBackground,
    surfaceContainerLow = PaperSurface,
    surfaceContainer = PaperSurfaceVariant,
    surfaceContainerHigh = Color(0xFFE3DDD2),
    surfaceContainerHighest = Color(0xFFD5CFC3),
    inverseSurface = TextPrimaryLight,
    inverseOnSurface = PaperSurface,
    inversePrimary = BronzeContainer
)

@Composable
fun NoteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    /** Системные dynamic colors отключаем по умолчанию — сохраняем фирменную палитру */
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = NoteShapes,
        content = content
    )
}
