package com.example.note.di

import com.example.note.domain.usecases.
import com.example.note.domain.usecases.AddNoteUseCase
import com.example.note.domain.usecases.EditNoteUseCase
import com.example.note.domain.usecases.GetNoteListUseCase
import com.example.note.domain.usecases.RemoveNoteUseCase
import org.koin.dsl.module

val domainModule = module {

    single<AddNoteUseCase> {
        AddNoteUseCase(noteListRepository = get())
    }

    single<EditNoteUseCase> {
        EditNoteUseCase(noteListRepository = get())
    }

    single<GetNoteListUseCase> {
        GetNoteListUseCase(noteListRepository = get())
    }

    single<RemoveNoteUseCase> {
        RemoveNoteUseCase(noteListRepository = get())
    }
}
