package com.example.presentation.noteWindow

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
import androidx.navigation.NavHostController
import com.example.notes.domain.Note
import com.example.presentation.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun DialogBeforeExit(controller: NavHostController,
                     mainViewModel: MainViewModel,
                     noteCopy: Note,
                     showDialog: MutableState<Boolean>,) {
    val scope = rememberCoroutineScope()
    AlertDialog(
        onDismissRequest = {showDialog.value = false},
        title = { Text(text = "Подтверждение действия") },
        text = { Text("Вы действительно хотите выйти без сохранения?") },
        confirmButton = {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,

                ){
                Button(
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray,
                        contentColor = Color.DarkGray
                    ), onClick = {
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
                    Text("Выйти", fontSize = 15.sp)
                }

                Button(
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.DarkGray,
                        contentColor = Color.LightGray
                    ), onClick = {
                        controller.navigateUp()
                        showDialog.value = false
                    }
                ) {
                    Text("Сохранить", fontSize = 15.sp)
                }
            }

        }
    )
}