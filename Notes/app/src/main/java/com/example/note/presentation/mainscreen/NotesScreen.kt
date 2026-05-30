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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
fun NotesScreen(
    controller: NavHostController,
    mainViewModel: MainViewModel,
    isSuper: Boolean = false,
    isSubscribed: Boolean = false,
    onAdminClick: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val listNote by mainViewModel.noteListFlow.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val scheme = MaterialTheme.colorScheme
    var showSubscriptionDialog by remember { mutableStateOf(false) }

    if (showSubscriptionDialog) {
        AlertDialog(
            onDismissRequest = { showSubscriptionDialog = false },
            title = { Text("Функция недоступна") },
            text = { Text("Голосовые заметки доступны только по подписке. Обратитесь к администратору для её активации.") },
            confirmButton = {
                TextButton(onClick = { showSubscriptionDialog = false }) {
                    Text("Понятно")
                }
            }
        )
    }

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
                                    // id=0 → Room сам генерирует уникальный id глобально
                                    val newId = mainViewModel.addNote(Note(0, "", ""))
                                    controller.navigate(DETAILS_PATH + newId)
                                }
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
                            if (isSubscribed) {
                                val intent = Intent(context, VoskTranscriptionScreen::class.java)
                                context.startActivity(intent)
                            } else {
                                showSubscriptionDialog = true
                            }
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_microphone),
                                contentDescription = "Транскрибация",
                                modifier = Modifier.size(28.dp),
                                tint = if (isSubscribed) scheme.primary else scheme.onSurfaceVariant
                            )
                        }
                        if (isSuper) {
                            IconButton(onClick = onAdminClick) {
                                Icon(
                                    imageVector = Icons.Default.AdminPanelSettings,
                                    contentDescription = "Админ",
                                    modifier = Modifier.size(26.dp),
                                    tint = scheme.primary
                                )
                            }
                        }
                        IconButton(onClick = onLogout) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Выйти",
                                modifier = Modifier.size(26.dp),
                                tint = scheme.onSurfaceVariant
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
