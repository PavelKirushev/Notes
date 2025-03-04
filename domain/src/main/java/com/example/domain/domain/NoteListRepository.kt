package com.example.notes.domain

import kotlinx.coroutines.flow.Flow

interface NoteListRepository {
    suspend fun addNote(noteModel: Note)

    suspend fun editNote(note: Note)

    suspend fun getNoteList(): Flow<List<Note>>

    suspend fun removeNote(id: Int)
}