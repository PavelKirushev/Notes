package com.example.note.presentation.mainscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.note.R
import com.example.note.domain.Note
import com.example.note.presentation.MainViewModel
import kotlinx.coroutines.launch

/**
 * Composable function for showing window before delete note
 * @param note Note for following note we want to delete
 * @param checkDelete MutableState<Boolean> true - show dialog, false - put away
 */
@Composable
fun DialogBeforeDelete(
    note: Note,
    checkDelete: MutableState<Boolean>,
) {
    val scope = rememberCoroutineScope()
    AlertDialog(
        onDismissRequest = {checkDelete.value = false},
        title = { Text(stringResource(R.string.confirm_title)) },
        text = { Text(stringResource(R.string.delete_note_confirm)) },
        confirmButton = {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, end = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,

                ){
                Button(
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray,
                        contentColor = Color.DarkGray
                    ), onClick = {
                        checkDelete.value = false
                    }
                ) {
                    Text(stringResource(R.string.cancel_button), fontSize = 15.sp)
                }

                Button(
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.DarkGray,
                        contentColor = Color.LightGray
                    ), onClick = {
                        scope.launch {
                            mainViewModel.removeNote(note.id)
                            checkDelete.value = false
                        }
                    }
                ) {
                    Text(stringResource(R.string.delete_button), fontSize = 15.sp)
                }
            }

        }
    )
}