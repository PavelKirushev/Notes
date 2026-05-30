package com.example.note.domain.usecases

import com.example.note.domain.Note
import com.example.note.domain.NoteListRepository

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
