package com.example.data

import androidx.room.Dao
import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface NoteDao {
    @Insert(entity = NoteEntity::class)
    suspend fun addNote(note: NoteEntity): Long
    @Update
    suspend fun editNote(note: NoteEntity)

    @Delete
    suspend fun delete(note: NoteEntity)

    @Query("SELECT * FROM notes")
    fun getNoteList(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNote(id: Int): NoteEntity?
}