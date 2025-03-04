package com.example.note.di

import com.example.domain.domain.usecases.AddNoteUseCase
import com.example.domain.domain.usecases.EditNoteUseCase
import com.example.domain.domain.usecases.GetNoteListUseCase
import com.example.domain.domain.usecases.RemoveNoteUseCase
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