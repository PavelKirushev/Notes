package com.example.data

import androidx.room.Database
import androidx.room.RoomDatabase

//класс для создания базы данных, содержащий ссылку на dao
@Database(version = 1, entities = [NoteEntity::class])
abstract class NoteDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao
}
