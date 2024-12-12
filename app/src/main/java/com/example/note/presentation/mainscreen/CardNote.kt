package com.example.note.presentation.mainscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.note.presentation.common.NoteText
import com.example.note.presentation.common.NoteTitleText
import com.example.notes.domain.Note

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
            onClick = { controller.navigate("details/" + note.id) }
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 30.dp, top = 5.dp, end = 20.dp)
            ) {
                NoteTitleText((note.id.toString() + " " + note.title))
                HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                NoteText(note.text)
            }
        }
    }
}