package com.example.note.presentation.mainscreen

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.note.R
import com.example.note.domain.Note
import com.example.note.presentation.MainViewModel

/**
 * Composable function for showing card of note
 *
 * @param controller NavHostController for navigation in app
 * @param note Note for showing note
 * @param mainViewModel MainViewModel to manage app state
 */
@Composable
fun CardNote(controller: NavHostController, note: Note, mainViewModel: MainViewModel) {
    val checkDelete = remember { mutableStateOf(false) }
    val scheme = MaterialTheme.colorScheme
    val shape = MaterialTheme.shapes.medium

    Column {
        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp,
                pressedElevation = 6.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = scheme.surfaceContainerHigh,
                contentColor = scheme.onSurface
            ),
            shape = shape,
            modifier = Modifier
                .padding(
                    start = dimensionResource(R.dimen.padding_20),
                    end = dimensionResource(R.dimen.padding_20),
                    top = 12.dp
                )
                .fillMaxWidth()
                .height(84.dp)
                .border(
                    width = 1.dp,
                    color = scheme.outline.copy(alpha = 0.18f),
                    shape = shape
                )
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            controller.navigate("details/" + note.id)
                        },
                        onLongPress = {
                            checkDelete.value = true
                        }
                    )
                }
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        start = dimensionResource(R.dimen.padding_30),
                        top = dimensionResource(R.dimen.padding_5),
                        end = dimensionResource(R.dimen.padding_20)
                    )
            ) {
                NoteTitleText(note.title)
                HorizontalDivider(
                    thickness = dimensionResource(R.dimen.padding_1),
                    color = scheme.outline.copy(alpha = 0.35f)
                )
                NoteText(note.text)
            }
        }
        if (checkDelete.value) {
            DialogBeforeDelete(mainViewModel, note, checkDelete)
        }
    }
}

@Composable
private fun NoteText(noteText: String) {
    Text(
        text = noteText,
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 1,
        modifier = Modifier.padding(top = 4.dp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun NoteTitleText(noteTitle: String) {
    Text(
        text = noteTitle,
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.2.sp
        ),
        maxLines = 1,
        modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_5)),
        color = MaterialTheme.colorScheme.onSurface,
        overflow = TextOverflow.Ellipsis
    )
}
