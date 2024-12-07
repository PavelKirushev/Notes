package com.example.notes.domain

class RemoveNoteUseCase(private val noteListRepository: NoteListRepository) {
    suspend fun removeNote(id: Int){
        noteListRepository.removeNote(id)
    }
}