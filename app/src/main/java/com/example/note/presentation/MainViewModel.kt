package com.example.note.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note.data.Dependencies
import com.example.notes.data.NoteEntity
import com.example.notes.data.NoteListRepositoryImpl
import com.example.notes.domain.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val repositoryImpl: NoteListRepositoryImpl): ViewModel() {

    suspend fun getNoteList(): List<Note> {
        return withContext(Dispatchers.IO) {
            repositoryImpl.getNoteList()
        }
    }

    suspend fun getNote(id: Int?): Note{
        if(id != null) {
            return withContext(Dispatchers.IO) {
                repositoryImpl.getNote(id)
            }
        }
        return Note(-1, "Ошибка", "id = null")
    }

}
