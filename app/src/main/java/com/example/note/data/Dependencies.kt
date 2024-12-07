package com.example.note.data

import android.content.Context
import androidx.room.Room
import com.example.notes.data.NoteDatabase

object Dependencies {

    private lateinit var applicationContext: Context

    fun init(context: Context) {
        applicationContext = context.applicationContext
    }

    private val appDatabase: NoteDatabase by lazy {
        Room.databaseBuilder(applicationContext, NoteDatabase::class.java, "database.db")
            .build()
    }

    fun provideNoteDao(): NoteDao = appDatabase.noteDao()
}