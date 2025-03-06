package com.example.presentation.noteWindow

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit

/**
 * Composable function for showing title/text of note
 *
 * @param text String title/text of note
 * @param onValueChange (String) -> Unit to follow if note was edited
 * @param fontSize TextUnit
 * @param modifier Modifier to manage app state modifiers for title and text are different
 */
@Composable
fun SimpleTextField(
    text: String,
    onValueChange: (String) -> Unit,
    fontSize: TextUnit,
    modifier: Modifier
) {
    BasicTextField(
        value = text,
        onValueChange = onValueChange,
        textStyle = TextStyle(color = Color.Black, fontSize = fontSize),
        singleLine = false,
        modifier = modifier
    )
}