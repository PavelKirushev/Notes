package com.example.presentation.mainscreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.common.Provider

@Composable
fun NoteText(noteText: String,
             fontSize: TextUnit = 15.sp,
             color: Color = Color.LightGray,
             overflow: TextOverflow = TextOverflow.Ellipsis){
    Text(
        text = noteText,
        fontSize = fontSize,
        maxLines = 1,
        modifier = Modifier
            .padding(top = 2.dp),
        color = color,
        overflow = overflow,
        fontFamily = FontFamily(
            Font(
            googleFont = GoogleFont("Open Sans"),
            fontProvider = Provider.getProvider()
            )
        )
    )
}