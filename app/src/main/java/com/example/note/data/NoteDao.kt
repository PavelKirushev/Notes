package com.example.note.data

import androidx.room.Dao
import androidx.room.*
import com.example.notes.data.NoteEntity


@Dao
interface NoteDao {
    @Insert(entity = NoteEntity::class)
    fun addNote(note: NoteEntity): Long

    @Update
    fun editNote(note: NoteEntity)

    @Delete
    fun delete(note: NoteEntity)

    @Query("SELECT * FROM notes")
    fun getNoteList(): List<NoteEntity>

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getNote(id: Int): NoteEntity?
}