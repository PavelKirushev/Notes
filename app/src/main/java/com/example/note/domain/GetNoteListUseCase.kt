package com.example.notes.domain

class GetNoteListUseCase(private val noteListRepository: NoteListRepository){
    suspend fun getNoteList(): List<Note>{
        return noteListRepository.getNoteList()
    }
}   