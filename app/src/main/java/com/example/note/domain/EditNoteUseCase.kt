package com.example.notes.domain

class EditNoteUseCase(private val noteListRepository: NoteListRepository) {
    suspend fun editNote(note: Note){
        noteListRepository.editNote(note)
    }
}