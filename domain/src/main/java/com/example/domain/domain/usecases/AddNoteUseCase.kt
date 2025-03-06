package com.example.domain.domain.usecases

import com.example.notes.domain.Note
import com.example.notes.domain.NoteListRepository

/**
 * Class to add new note
 *
 *  @param noteListRepository - class that realizes interface NoteListRepository
 */
class AddNoteUseCase(private val noteListRepository: NoteListRepository) {
    suspend fun addNote(note: Note){
        noteListRepository.addNote(note)
    }
}