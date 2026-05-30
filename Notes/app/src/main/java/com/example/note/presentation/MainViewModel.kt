package com.example.note.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note.domain.Note
import com.example.note.domain.usecases.AddNoteUseCase
import com.example.note.domain.usecases.EditNoteUseCase
import com.example.note.domain.usecases.GetNoteListUseCase
import com.example.note.domain.usecases.RemoveNoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val addNoteUseCase: AddNoteUseCase,
    private val editNoteUseCase: EditNoteUseCase,
    private val getNoteListUseCase: GetNoteListUseCase,
    private val removeNoteUseCase: RemoveNoteUseCase
) : ViewModel() {

    private val _noteListFlow = MutableStateFlow<List<Note>>(emptyList())
    val noteListFlow: StateFlow<List<Note>> = _noteListFlow

    init {
        viewModelScope.launch {
            getNoteListUseCase.getNoteList().collect { notes ->
                _noteListFlow.value = notes
            }
        }
    }

    // Сохраняет заметку и возвращает id сгенерированный Room (autoGenerate)
    suspend fun addNote(newNote: Note): Int {
        return addNoteUseCase.addNote(newNote)
    }

    suspend fun editNote(updatedNote: Note) {
        editNoteUseCase.editNote(updatedNote)
        val updatedList = _noteListFlow.value.map { note ->
            if (note.id == updatedNote.id) updatedNote else note
        }
        _noteListFlow.value = updatedList
    }

    suspend fun removeNote(noteId: Int) {
        try {
            removeNoteUseCase.removeNote(noteId)
            val updatedList = _noteListFlow.value.filter { it.id != noteId }
            _noteListFlow.value = updatedList
        } catch (e: NoSuchElementException) {
            Log.e(TAG, "Note with id $noteId not found")
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}
