package com.example.data

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Class for creating DB with noteDao function that returns NoteDao interface implementation class
 *
 */
@Database(version = 1, entities = [NoteEntity::class])
abstract class NoteDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao
}
