package com.example.note.di

import android.content.Context
import androidx.room.Room
import com.example.data.data.NoteDao
import com.example.notes.data.NoteDatabase
import com.example.notes.data.NoteListRepositoryImpl
import com.example.notes.domain.NoteListRepository
import org.koin.dsl.module

val dataModule = module {

    single<NoteListRepository>{
        NoteListRepositoryImpl(noteDao = get())
    }

    single<NoteDatabase>{
        Room.databaseBuilder(
            context = get<Context>(),
            klass = NoteDatabase::class.java,
            name = "database.db"
        ).build()
    }

    single<NoteDao>{
        get<NoteDatabase>().noteDao()
    }

}
