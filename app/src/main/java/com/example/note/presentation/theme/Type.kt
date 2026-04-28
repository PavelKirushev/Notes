package com.example.note.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.example.note.presentation.common.GoogleFontProvider

private const val FONT = "Plus Jakarta Sans"

private val PlusJakarta = GoogleFont(FONT)

private val NoteFontFamily = FontFamily(
    Font(googleFont = PlusJakarta, fontProvider = GoogleFontProvider.getProvider(),
         weight = FontWeight.Normal, style = FontStyle.Normal),
    Font(googleFont = PlusJakarta, fontProvider = GoogleFontProvider.getProvider(),
         weight = FontWeight.Medium, style = FontStyle.Normal),
    Font(googleFont = PlusJakarta, fontProvider = GoogleFontProvider.getProvider(),
         weight = FontWeight.SemiBold, style = FontStyle.Normal),
    Font(googleFont = PlusJakarta, fontProvider = GoogleFontProvider.getProvider(),
         weight = FontWeight.Bold, style = FontStyle.Normal)
)

private val base = Typography()

val Typography = Typography(
    displayLarge = base.displayLarge.copy(fontFamily = NoteFontFamily),
    displayMedium = base.displayMedium.copy(fontFamily = NoteFontFamily),
    displaySmall = base.displaySmall.copy(fontFamily = NoteFontFamily),
    headlineLarge = base.headlineLarge.copy(fontFamily = NoteFontFamily),
    headlineMedium = base.headlineMedium.copy(fontFamily = NoteFontFamily),
    headlineSmall = base.headlineSmall.copy(fontFamily = NoteFontFamily),
    titleLarge = base.titleLarge.copy(fontFamily = NoteFontFamily),
    titleMedium = base.titleMedium.copy(fontFamily = NoteFontFamily),
    titleSmall = base.titleSmall.copy(fontFamily = NoteFontFamily),
    bodyLarge = base.bodyLarge.copy(fontFamily = NoteFontFamily),
    bodyMedium = base.bodyMedium.copy(fontFamily = NoteFontFamily),
    bodySmall = base.bodySmall.copy(fontFamily = NoteFontFamily),
    labelLarge = base.labelLarge.copy(fontFamily = NoteFontFamily),
    labelMedium = base.labelMedium.copy(fontFamily = NoteFontFamily),
    labelSmall = base.labelSmall.copy(fontFamily = NoteFontFamily)
)
