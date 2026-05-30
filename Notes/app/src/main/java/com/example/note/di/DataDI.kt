package com.example.note.di

import android.content.Context
import androidx.room.Room
import com.example.note.data.room.NoteDao
import com.example.note.data.room.NoteDatabase
import com.example.note.data.room.NoteListRepositoryImpl
import com.example.note.domain.NoteListRepository
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
