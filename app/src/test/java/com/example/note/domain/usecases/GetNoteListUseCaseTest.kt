package com.example.note.domain.usecases

import com.example.note.domain.Note
import com.example.note.domain.NoteListRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*

class GetNoteListUseCaseTest {

    @Test
    fun `getNoteList should call repository getNoteList`() = runTest {
        val repository = mockk<NoteListRepository>(relaxed = true)
        val useCase = GetNoteListUseCase(repository)
        val notes = listOf(Note(1, "Test title", "Test content"))

        coEvery { repository.getNoteList() } returns flowOf(notes)

        val result = useCase.getNoteList()

        coVerify(exactly = 1) { repository.getNoteList() }
        assertSame(result, repository.getNoteList())
    }
}