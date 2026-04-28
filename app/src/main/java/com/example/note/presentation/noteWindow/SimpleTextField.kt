package com.example.note.presentation.noteWindow

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    val colorScheme = MaterialTheme.colorScheme
    BasicTextField(
        value = text,
        onValueChange = onValueChange,
        textStyle = TextStyle(
            color = colorScheme.onSurface,
            fontSize = fontSize,
            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily
        ),
        singleLine = false,
        modifier = modifier
    )
}
