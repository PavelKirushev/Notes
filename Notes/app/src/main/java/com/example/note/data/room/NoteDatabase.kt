package com.example.note.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(version = 2, entities = [NoteEntity::class])
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}

// Добавляем userId к существующим заметкам (DEFAULT 0 — старые заметки станут невидимы
// пока пользователь не войдёт; при первом входе его userId сохраняется и он видит свои)
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE notes ADD COLUMN userId INTEGER NOT NULL DEFAULT 0")
    }
}
