package com.example.notes.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.data.NoteDao

//класс для создания базы данных, содержащий ссылку на dao
@Database(version = 1, entities = [NoteEntity::class])
abstract class NoteDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao
}
