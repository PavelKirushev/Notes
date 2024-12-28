package com.example.domain.domain.usecases

import com.example.notes.domain.Note
import com.example.notes.domain.NoteListRepository

class GetNoteUseCase(private val noteListRepository: NoteListRepository) {
    suspend fun getNote(id: Int): Note {
        return noteListRepository.getNote(id)
    }
}