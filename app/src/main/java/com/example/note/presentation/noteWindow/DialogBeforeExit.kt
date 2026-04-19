package com.example.note.presentation.noteWindow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.note.R
import com.example.note.domain.Note
import com.example.note.presentation.MainViewModel
import kotlinx.coroutines.launch

/**
 * Composable function for showing window before exit on main screen
 *
 * @param controller NavHostController for navigation in app
 * @param mainViewModel MainViewModel to manage app state
 * @param noteCopy Note for editing note in DB when we don't want to save change
 * @param showDialog MutableState<Boolean> true - show, false - put away
 */
@Composable
fun DialogBeforeExit(
    controller: NavHostController,
    mainViewModel: MainViewModel,
    noteCopy: Note,
    showDialog: MutableState<Boolean>,
) {
    val scope = rememberCoroutineScope()
    val scheme = MaterialTheme.colorScheme
    AlertDialog(
        onDismissRequest = { showDialog.value = false },
        containerColor = scheme.surfaceContainerHigh,
        titleContentColor = scheme.onSurface,
        textContentColor = scheme.onSurfaceVariant,
        title = { Text(stringResource(R.string.confirm_title)) },
        text = { Text(stringResource(R.string.exit_confirm)) },
        confirmButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = dimensionResource(R.dimen.padding_15),
                        end = dimensionResource(R.dimen.padding_20),
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Button(
                    shape = RoundedCornerShape(dimensionResource(R.dimen.padding_15)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = scheme.surfaceContainerHighest,
                        contentColor = scheme.onSurface
                    ),
                    onClick = {
                        scope.launch {
                            if (noteCopy.title.isEmpty() && noteCopy.text.isEmpty()) {
                                mainViewModel.removeNote(noteCopy.id)
                            } else {
                                mainViewModel.editNote(noteCopy)
                            }
                            controller.navigateUp()
                            showDialog.value = false
                        }
                    }
                ) {
                    Text(stringResource(R.string.exit_button), fontSize = 15.sp)
                }

                Button(
                    shape = RoundedCornerShape(dimensionResource(R.dimen.padding_15)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = scheme.primaryContainer,
                        contentColor = scheme.onPrimaryContainer
                    ),
                    onClick = {
                        controller.navigateUp()
                        showDialog.value = false
                    }
                ) {
                    Text(stringResource(R.string.save_button), fontSize = 15.sp)
                }
            }
        }
    )
}
