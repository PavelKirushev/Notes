package com.example.presentation.presentation.common

import androidx.compose.ui.text.googlefonts.GoogleFont
import com.example.common.R

object Provider {
    fun getProvider(): GoogleFont.Provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )

}