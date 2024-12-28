package com.example.data.data

import androidx.room.Dao
import androidx.room.*
import com.example.notes.data.NoteEntity


@Dao
interface NoteDao {
    @Insert(entity = NoteEntity::class)
    suspend fun addNote(note: NoteEntity): Long

    @Update
    suspend fun editNote(note: NoteEntity)

    @Delete
    suspend fun delete(note: NoteEntity)

    @Query("SELECT * FROM notes")
    suspend fun getNoteList(): List<NoteEntity>

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNote(id: Int): NoteEntity?
}