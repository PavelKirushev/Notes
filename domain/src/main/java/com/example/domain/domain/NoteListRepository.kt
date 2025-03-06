package com.example.notes.domain

import kotlinx.coroutines.flow.Flow

/**
 * Interface that needed to realize it on data layer and work with the DB
 *
 */
interface NoteListRepository {
    suspend fun addNote(note: Note)

    suspend fun editNote(note: Note)

    suspend fun getNoteList(): Flow<List<Note>>

    suspend fun removeNote(id: Int)
}