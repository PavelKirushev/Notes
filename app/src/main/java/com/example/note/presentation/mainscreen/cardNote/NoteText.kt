package com.example.note.presentation.mainscreen.cardNote

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.note.presentation.Provider

@Composable
fun NoteText(noteText: String){
    Text(
        text = noteText,
        fontSize = 15.sp,
        maxLines = 1,
        modifier = Modifier
            .padding(top = 2.dp),
        overflow = TextOverflow.Ellipsis,
        fontFamily = FontFamily(
            Font(
            googleFont = GoogleFont("Open Sans"),
            fontProvider = Provider.getProvider())
        )
    )
}