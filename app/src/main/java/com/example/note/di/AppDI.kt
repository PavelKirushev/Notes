package com.example.note.di

import com.example.note.presentation.MainViewModel
import org.koin.dsl.module

val appModule = module {

    single <MainViewModel>{
        MainViewModel(
            addNoteUseCase = get(),
            editNoteUseCase = get(),
            getNoteListUseCase = get(),
            removeNoteUseCase = get()
        )
    }
}
