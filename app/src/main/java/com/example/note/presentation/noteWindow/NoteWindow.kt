package com.example.note.presentation.noteWindow

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.note.R
import com.example.note.domain.Note
import com.example.note.presentation.MainViewModel
import kotlinx.coroutines.launch

/**
 * Composable function to add/edit/delete(if empty) note
 *
 * @param controller NavHostController for navigation in app
 * @param note Note for show if it already exists
 * @param mainViewModel MainViewModel to manage app state
 */
@Composable
fun NoteWindow(controller: NavHostController, note: Note, mainViewModel: MainViewModel) {
    val scope = rememberCoroutineScope()
    var title by remember { mutableStateOf(note.title) }
    var text by remember { mutableStateOf(note.text) }
    var checkChange by remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }
    val noteCopy by remember {
        mutableStateOf(
            Note(
                note.id,
                note.title,
                note.text
            )
        )
    }
    LaunchedEffect(title, text) {
        val updatedNote = note.copy(title = title, text = text)
        scope.launch {
            mainViewModel.editNote(updatedNote)
        }
    }

    val scheme = MaterialTheme.colorScheme

    Column(modifier = Modifier.fillMaxSize()) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = scheme.surfaceContainerHigh,
            tonalElevation = 2.dp,
            shadowElevation = 0.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_10))
                        .clickable {
                            if (checkChange) {
                                showDialog.value = true
                            } else {
                                if (title.isEmpty() && text.isEmpty()) {
                                    scope.launch {
                                        mainViewModel.removeNote(note.id)
                                    }
                                }
                                controller.navigateUp()
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_back_white),
                        contentDescription = stringResource(R.string.back_button),
                        Modifier.size(dimensionResource(R.dimen.padding_30)),
                        tint = scheme.primary
                    )
                    Text(
                        text = stringResource(R.string.back_button),
                        fontSize = 24.sp,
                        color = scheme.onSurface
                    )
                }
                if (checkChange) {
                    Icon(
                        painter = painterResource(id = R.drawable.done),
                        contentDescription = stringResource(R.string.ready_button),
                        Modifier
                            .size(45.dp)
                            .padding(
                                top = dimensionResource(R.dimen.padding_7),
                                end = dimensionResource(R.dimen.padding_10)
                            )
                            .clickable {
                                controller.navigateUp()
                            },
                        tint = scheme.primary
                    )
                }
                if (showDialog.value) {
                    DialogBeforeExit(controller, mainViewModel, noteCopy, showDialog)
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(
                    top = dimensionResource(R.dimen.padding_20),
                    start = dimensionResource(R.dimen.padding_30),
                    end = dimensionResource(R.dimen.padding_20),
                    bottom = dimensionResource(R.dimen.padding_30)
                )
        ) {
            SimpleTextField(
                text = title,
                fontSize = 40.sp,
                onValueChange = {
                    title = it
                    checkChange = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(R.dimen.padding_20))
            )
            SimpleTextField(
                text = text,
                fontSize = 20.sp,
                onValueChange = {
                    text = it
                    checkChange = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = dimensionResource(R.dimen.padding_20))
            )
        }
    }
}
