package com.example.domain.domain.usecases

import com.example.notes.domain.NoteListRepository

class RemoveNoteUseCase(private val noteListRepository: NoteListRepository) {
    suspend fun removeNote(id: Int){
        noteListRepository.removeNote(id)
    }
}