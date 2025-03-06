package com.example.domain.domain.usecases

import com.example.notes.domain.Note
import com.example.notes.domain.NoteListRepository

/**
 * Class to edit note
 *
 *  @param noteListRepository - class that realizes interface NoteListRepository
 */
class EditNoteUseCase(private val noteListRepository: NoteListRepository) {
    suspend fun editNote(note: Note){
        noteListRepository.editNote(note)
    }
}