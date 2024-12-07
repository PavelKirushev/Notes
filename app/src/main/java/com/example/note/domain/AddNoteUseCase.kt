package com.example.notes.domain

class AddNoteUseCase(private val noteListRepository: NoteListRepository) {
    suspend fun addNote(note: Note){
        noteListRepository.addNote(note)
    }
}