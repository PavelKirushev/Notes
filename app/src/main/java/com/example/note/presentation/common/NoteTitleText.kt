package com.example.note.presentation.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NoteTitleText(noteTitle: String,
                  fontSize: TextUnit = 20.sp,
                  color: Color = Color.White,
                  overflow: TextOverflow = TextOverflow.Ellipsis){
    Text(
        text = noteTitle,
        fontSize = fontSize,
        maxLines = 1,
        modifier = Modifier
            .padding(top = 5.dp),
        color = color,
        overflow = overflow,
        fontFamily = FontFamily(
            Font(
            googleFont = GoogleFont("Open Sans"),
            fontProvider = Provider.getProvider(),
            weight = FontWeight.Bold)
        )
    )
}