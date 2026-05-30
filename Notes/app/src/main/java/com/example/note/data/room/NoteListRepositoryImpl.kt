package com.example.note.data.room

import com.example.note.domain.Note
import com.example.note.domain.NoteListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementation NoteListRepository in domain layer
 *
 * @param noteDao - Data access object for CRUD in the DB
 *
 */
class NoteListRepositoryImpl(private val noteDao: NoteDao) : NoteListRepository {
    override suspend fun addNote(note: Note) {
        noteDao.addNote(note.toEntity())
    }

    override suspend fun editNote(note: Note) {
        noteDao.editNote(note.toEntity())
    }

    override suspend fun getNoteList(): Flow<List<Note>> {
        return noteDao.getNoteList().map { notes ->
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
