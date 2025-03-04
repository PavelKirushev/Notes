package com.example.presentation.noteWindow

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun SimpleTextField(
    text: String,
    onValueChange: (String) -> Unit,
    fontSize: TextUnit
) {
    BasicTextField(
        value = text,
        onValueChange = onValueChange,
        textStyle = TextStyle(color = Color.Black, fontSize = fontSize),
        singleLine = false,
        modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
    )
}