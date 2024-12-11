package com.example.note.presentation.mainscreen.cardNote

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.notes.domain.Note
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date

@Composable
fun CardNote(controller: NavHostController, note: Note = Note(-1, "testTtestTitletestTitletestTitletestTitleitle", "testText"),) {
    Column {
        Card(
            elevation = CardDefaults.cardElevation(5.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.DarkGray,
                contentColor = Color.LightGray
            ),
            modifier = Modifier
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 10.dp
                )
                .fillMaxWidth()
                .height(70.dp),
            onClick = { controller.navigate("details/1") }
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 30.dp, top = 5.dp, end = 20.dp)
            ) {
                NoteTitleText((note.id.toString() + " " + note.title))
                NoteText(note.text)
            }
        }
    }
}