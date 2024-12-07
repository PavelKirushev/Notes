package com.example.note.presentation

import androidx.lifecycle.ViewModel
import com.example.note.data.Dependencies
import com.example.notes.data.NoteEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(): ViewModel() {
    suspend fun getNotesFromDatabase(): List<NoteEntity> {
        // This assumes you have a valid Dao instance set up
        val dao = Dependencies.provideNoteDao()
        return withContext(Dispatchers.IO) {
            dao.getNoteList() // Fetch notes asynchronously
        }
    }

}

fun deleteTestData(id: Int){
    CoroutineScope(Dispatchers.IO).launch {
        val noteDao = Dependencies.provideNoteDao()

        val data = noteDao.getNoteList()

        data.forEach {
            if (it.id == id){
                noteDao.delete(it)
            }
        }
    }
}

fun insertTestData() {
    CoroutineScope(Dispatchers.IO).launch {
        val noteDao = Dependencies.provideNoteDao()

        val testNotes = listOf(
            NoteEntity(title = "Test Note 1", text = "This is the first test note."),
            NoteEntity(title = "Test Note 2", text = "This is the second test note.")
        )

        testNotes.forEach {
            noteDao.addNote(it)
        }
    }
}