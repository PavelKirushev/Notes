package com.example.data

import com.example.notes.data.toDomain
import com.example.notes.data.toEntity
import com.example.notes.domain.Note
import com.example.notes.domain.NoteListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//связывает доменный слой со слоем данных
class NoteListRepositoryImpl(private val noteDao: NoteDao) : NoteListRepository {
    //Room использует пул потоков, который уже настроен на работу с базой данных
    //поэтому объявление скоупа и диспатчера ио не потребуется
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