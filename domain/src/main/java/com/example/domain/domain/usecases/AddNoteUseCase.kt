package com.example.domain.domain.usecases

import com.example.notes.domain.Note
import com.example.notes.domain.NoteListRepository

class AddNoteUseCase(private val noteListRepository: NoteListRepository) {
    suspend fun addNote(note: Note){
        noteListRepository.addNote(note)
    }
}