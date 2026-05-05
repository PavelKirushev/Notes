package com.example.note.domain.usecases

import com.example.note.domain.NoteListRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*

class RemoveNoteUseCaseTest {

    @Test
    fun `removeNote should call repository removeNote`() = runTest {
        val repository = mockk<NoteListRepository>(relaxed = true)
        val useCase = RemoveNoteUseCase(repository)
        val noteId = 1

        useCase.removeNote(noteId)

        coVerify(exactly = 1) { repository.removeNote(noteId) }
    }
}