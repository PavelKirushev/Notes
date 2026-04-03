package com.example.note.presentation.mainscreen

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.note.R
import com.example.note.domain.Note
import com.example.note.presentation.MainViewModel
import com.example.note.presentation.common.Provider

private const val OPEN_SANS = "Open Sans"

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
    Column {
        Card(
            elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.padding_5)),
            colors = CardDefaults.cardColors(
                containerColor = Color.DarkGray,
                contentColor = Color.LightGray
            ),
            modifier = Modifier
                .padding(
                    start = dimensionResource(R.dimen.padding_20),
                    end = dimensionResource(R.dimen.padding_20),
                    top = 10.dp
                )
                .fillMaxWidth()
                .height(70.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            controller.navigate("details/" + note.id)
                                },
                        onLongPress = {
                            checkDelete.value = true
                        }
                    )
                },
        ) {
            Column(
                modifier = Modifier
                    .padding(start = dimensionResource(R.dimen.padding_30), top = dimensionResource(R.dimen.padding_5), end = dimensionResource(R.dimen.padding_20))
            ) {
                NoteTitleText(note.title)
                HorizontalDivider(thickness = dimensionResource(R.dimen.padding_1), color = Color.Gray)
                NoteText(note.text)
            }
        }
        if (checkDelete.value) {
            DialogBeforeDelete(note, checkDelete)
        }
    }
}

@Composable
private fun NoteText(noteText: String){
    Text(
        text = noteText,
        fontSize = 15.sp,
        maxLines = 1,
        modifier = Modifier
            .padding(top = 2.dp),
        color = Color.LightGray,
        overflow = TextOverflow.Ellipsis,
        fontFamily = FontFamily(
            Font(
                googleFont = GoogleFont(OPEN_SANS),
                fontProvider = Provider.getProvider()
            )
        )
    )
}

@Composable
private fun NoteTitleText(noteTitle: String,){
    Text(
        text = noteTitle,
        fontSize = 20.sp,
        maxLines = 1,
        modifier = Modifier
            .padding(top = dimensionResource(R.dimen.padding_5)),
        color = Color.White,
        overflow = TextOverflow.Ellipsis,
        fontFamily = FontFamily(
            Font(
                googleFont = GoogleFont(OPEN_SANS),
                fontProvider = Provider.getProvider(),
                weight = FontWeight.Bold)
        )
    )
}