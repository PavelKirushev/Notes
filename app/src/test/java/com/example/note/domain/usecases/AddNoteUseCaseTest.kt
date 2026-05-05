package com.example.note.domain.usecases

import com.example.note.domain.Note
import com.example.note.domain.NoteListRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*

class AddNoteUseCaseTest {

    @Test
    fun `addNote should call repository addNote`() = runTest {
        val repository = mockk<NoteListRepository>(relaxed = true)
        val useCase = AddNoteUseCase(repository)
        val note = Note(1, "Test title", "Test content")

        useCase.addNote(note)

        coVerify(exactly = 1) { repository.addNote(note) }
    }
}