package com.example.note.presentation

import androidx.compose.ui.text.googlefonts.GoogleFont
import com.example.note.R

object Provider {
    fun getProvider(): GoogleFont.Provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )
}