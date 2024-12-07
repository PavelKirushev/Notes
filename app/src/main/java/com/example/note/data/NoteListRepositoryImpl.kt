package com.example.notes.data

import androidx.room.Dao
import com.example.note.data.NoteDao
import com.example.notes.domain.Note
import com.example.notes.domain.NoteListRepository

//связывает доменный слой со слоем данных
@Dao
class NoteListRepositoryImpl(private val noteDao: NoteDao) : NoteListRepository {
    //Room использует пул потоков, который уже настроен на работу с базой данных
    //поэтому объявление скоупа и диспатчера ио не потребуется
    override suspend fun addNote(note: Note) {
        noteDao.addNote(note.toEntity())
    }

    override suspend fun editNote(note: Note) {
        noteDao.editNote(note.toEntity())
    }

    override suspend fun getNoteList(): List<Note> {
        return noteDao.getNoteList().map { it.toDomain() }
    }

    override suspend fun getNote(id: Int): Note {
        return noteDao.getNote(id)?.toDomain() ?: throw NoSuchElementException("Note not found")
    }


    override suspend fun removeNote(id: Int) {
        val note = noteDao.getNote(id)
        note?.let {
            noteDao.delete(it)
        } ?: throw NoSuchElementException("Note not found")
    }

}