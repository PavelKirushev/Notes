package com.example.note.domain.usecases

import com.example.note.domain.Note
import com.example.note.domain.NoteListRepository
import kotlinx.coroutines.flow.Flow

/**
 * Class to get all notes in DB
 *
 *  @param noteListRepository - class that realizes interface NoteListRepository
 */
class GetNoteListUseCase(private val noteListRepository: NoteListRepository){
    suspend fun getNoteList(): Flow<List<Note>> {
        return noteListRepository.getNoteList()
    }
}
