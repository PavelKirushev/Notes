package com.example.note.data.room

import com.example.note.data.preferences.TokenStorage
import com.example.note.domain.Note
import com.example.note.domain.NoteListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteListRepositoryImpl(
    private val noteDao: NoteDao,
    private val tokenStorage: TokenStorage
) : NoteListRepository {

    private val userId: Long get() = tokenStorage.getUserId()

    override suspend fun addNote(note: Note): Int {
        return noteDao.addNote(note.toEntity(userId)).toInt()
    }

    override suspend fun editNote(note: Note) {
        noteDao.editNote(note.toEntity(userId))
    }

    override suspend fun getNoteList(): Flow<List<Note>> {
        return noteDao.getNoteList(userId).map { notes ->
            notes.map { it.toDomain() }
        }
    }

    override suspend fun removeNote(id: Int) {
        val note = noteDao.getNote(id)
        note?.let {
            noteDao.delete(it)
        } ?: throw NoSuchElementException("Note not found")
    }
}
