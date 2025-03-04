package com.example.presentation.mainscreen

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notes.domain.Note
import com.example.presentation.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun DialogBeforeDelete(mainViewModel: MainViewModel,
                       note: Note,
                       checkDelete: MutableState<Boolean>) {
    val scope = rememberCoroutineScope()
    AlertDialog(
        onDismissRequest = {checkDelete.value = false},
        title = { Text(text = "Подтверждение действия") },
        text = { Text("Вы действительно хотите удалить заметку?") },
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
                    Text("Не удалять", fontSize = 15.sp)
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
                    Text("Удалить", fontSize = 15.sp)
                }
            }

        }
    )
}