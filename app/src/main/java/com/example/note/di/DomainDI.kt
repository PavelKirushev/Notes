package com.example.note.di

import com.example.note.domain.usecases.*
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