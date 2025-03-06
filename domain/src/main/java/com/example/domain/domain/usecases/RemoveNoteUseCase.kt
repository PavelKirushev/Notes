package com.example.domain.domain.usecases

import com.example.notes.domain.NoteListRepository

/**
 * Class to delete note
 *
 *  @param noteListRepository - class that realizes interface NoteListRepository
 */
class RemoveNoteUseCase(private val noteListRepository: NoteListRepository) {
    suspend fun removeNote(id: Int){
        noteListRepository.removeNote(id)
    }
}