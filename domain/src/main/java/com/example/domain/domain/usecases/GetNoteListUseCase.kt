package com.example.domain.domain.usecases

import com.example.notes.domain.Note
import com.example.notes.domain.NoteListRepository

class GetNoteListUseCase(private val noteListRepository: NoteListRepository){
    suspend fun getNoteList(): List<Note>{
        return noteListRepository.getNoteList()
    }
}   