package com.example.notes.domain

class GetNoteUseCase(private val noteListRepository: NoteListRepository) {
    suspend fun getNote(id: Int): Note{
        return noteListRepository.getNote(id)
    }
}