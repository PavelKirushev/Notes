package com.example.note.presentation.mainscreen

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.note.R
import com.example.note.domain.Note
import com.example.note.presentation.MainViewModel
import com.example.note.presentation.vosk.VoskTranscriptionScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val DETAILS_PATH = "details/"
private const val X_OFFSET = 1200f
private const val Y_OFFSET = 1600f

/**
 * Composable function for showing all notes on main screen
 *
 * @param controller NavHostController for navigation in app
 * @param mainViewModel mainViewModel to manage app state
 */
@Composable
fun NotesScreen(controller: NavHostController, mainViewModel: MainViewModel) {
    val listNote by mainViewModel.noteListFlow.collectAsState()
    val scope = CoroutineScope(Dispatchers.IO)
    val context = LocalContext.current
    val scheme = MaterialTheme.colorScheme

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        scheme.background,
                        scheme.surfaceContainer,
                        scheme.surfaceContainerHigh.copy(alpha = 0.35f)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(X_OFFSET, Y_OFFSET)
                )
            )
    ) {
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = scheme.surfaceContainerHigh,
                tonalElevation = 2.dp,
                shadowElevation = 0.dp
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(start = 8.dp, end = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.screen_notes_title),
                        style = MaterialTheme.typography.headlineSmall,
                        color = scheme.onSurface,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    mainViewModel.addNote(
                                        Note(
                                            mainViewModel.getNextNoteId(),
                                            "",
                                            ""
                                        )
                                    )
                                }
                                controller.navigate(DETAILS_PATH + mainViewModel.getNextNoteId())
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.edit_white),
                                contentDescription = stringResource(R.string.edit_button),
                                modifier = Modifier.size(28.dp),
                                tint = scheme.primary
                            )
                        }
                        IconButton(onClick = {
                            val intent = Intent(context, VoskTranscriptionScreen::class.java)
                            context.startActivity(intent)
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_microphone),
                                contentDescription = "Транскрибация",
                                modifier = Modifier.size(28.dp),
                                tint = scheme.primary
                            )
                        }
                    }
                }
            }
        }

        items(
            items = listNote,
            key = { note -> note.id }
        ) { note ->
            CardNote(controller, note, mainViewModel)
        }
    }
}
