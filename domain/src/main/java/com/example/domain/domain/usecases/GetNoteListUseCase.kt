package com.example.domain.domain.usecases

import com.example.notes.domain.Note
import com.example.notes.domain.NoteListRepository
import kotlinx.coroutines.flow.Flow

class GetNoteListUseCase(private val noteListRepository: NoteListRepository){
    suspend fun getNoteList(): Flow<List<Note>> {
        return noteListRepository.getNoteList()
    }
}   