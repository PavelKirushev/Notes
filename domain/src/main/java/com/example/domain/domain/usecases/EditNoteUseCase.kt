package com.example.domain.domain.usecases

import com.example.notes.domain.Note
import com.example.notes.domain.NoteListRepository

class EditNoteUseCase(private val noteListRepository: NoteListRepository) {
    suspend fun editNote(note: Note){
        noteListRepository.editNote(note)
    }
}