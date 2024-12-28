package com.example.notes.domain

interface NoteListRepository {
    suspend fun addNote(noteModel: Note)

    suspend fun editNote(note: Note)

    suspend fun getNoteList(): List<Note>

    suspend fun getNote(id: Int): Note

    suspend fun removeNote(id: Int)
}