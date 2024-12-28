package com.example.presentation.presentation

import androidx.lifecycle.ViewModel
import com.example.domain.domain.usecases.AddNoteUseCase
import com.example.domain.domain.usecases.EditNoteUseCase
import com.example.domain.domain.usecases.GetNoteListUseCase
import com.example.domain.domain.usecases.GetNoteUseCase
import com.example.domain.domain.usecases.RemoveNoteUseCase
import com.example.notes.domain.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(private val addNoteUseCase: AddNoteUseCase,
                    private val editNoteUseCase: EditNoteUseCase,
                    private val getNoteUseCase: GetNoteUseCase,
                    private val getNoteListUseCase: GetNoteListUseCase,
                    private val removeNoteUseCase: RemoveNoteUseCase,
                    ): ViewModel() {
    private val _noteFlow = MutableStateFlow(Note(-1, "...", "..."))
    val noteFlow = _noteFlow.asStateFlow()

//    fun getNoteList(): List<Note> {
//        return withContext(Dispatchers.IO) {
//            repository.getNoteList()
//        }
//    }
//
//    suspend fun getNote(id: Int?) = withContext(Dispatchers.IO){
//        if(id != null){
//            _noteFlow.value = repository.getNote(id)
//        }
//    }
//
//    suspend fun editNote(note: Note) = withContext(Dispatchers.IO){
//        repository.editNote(note)
//        _noteFlow.value = note
//    }
//
//    suspend fun addNote(note: Note) = withContext(Dispatchers.IO){
//        repository.addNote(note)
//        _noteFlow.value = note
//    }
    suspend fun addNote(note: Note) {
        addNoteUseCase.addNote(note)
        _noteFlow.value = note
    }

    suspend fun getNoteList(): List<Note> {
        return getNoteListUseCase.getNoteList()
    }

    suspend fun getNote(id: Int?) {
        if(id != null){
            val note = getNoteUseCase.getNote(id)
            _noteFlow.value = note
        }
    }

    suspend fun editNote(note: Note) {
        editNoteUseCase.editNote(note)
        _noteFlow.value = note
    }

    suspend fun removeNote(id: Int?){
        if(id != null){
            removeNoteUseCase.removeNote(id)
        }
    }
}
