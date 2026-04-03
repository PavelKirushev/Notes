package com.example.note.domain.usecases

import com.example.note.domain.NoteListRepository

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