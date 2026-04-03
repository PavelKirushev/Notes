package com.example.note.domain.usecases

import com.example.note.domain.Note
import com.example.note.domain.NoteListRepository

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